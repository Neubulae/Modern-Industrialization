/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.machines.impl.multiblock;

import static aztech.modern_industrialization.machines.impl.multiblock.HatchType.*;

import aztech.modern_industrialization.machines.impl.MachineBlockEntity;
import aztech.modern_industrialization.machines.impl.MachineFactory;
import aztech.modern_industrialization.util.NbtHelper;
import net.fabricmc.fabric.api.lookup.v1.item.ItemKey;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidApi;
import net.fabricmc.fabric.api.transfer.v1.item.ItemApi;
import net.fabricmc.fabric.api.transfer.v1.storage.Movement;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

public class HatchBlockEntity extends MachineBlockEntity {
    private BlockPos controllerPos = null;
    /**
     * This variable is use to lazily sync controllerPos, to prevent needless
     * updates when the controller checks the shape!
     */
    private BlockPos lastSyncedControllerPos = null;
    private boolean lateLoaded = false;
    public final HatchType type;

    public HatchBlockEntity(MachineFactory factory, HatchType type) {
        super(factory);
        this.type = type;
    }

    private void lateLoad() {
        if (lateLoaded)
            return;
        lateLoaded = true;
        clearLocks();
        if (controllerPos != null) {
            BlockEntity controllerEntity = world.getBlockEntity(controllerPos);
            controllerPos = null;
            if (controllerEntity instanceof MultiblockMachineBlockEntity) {
                ((MultiblockMachineBlockEntity) controllerEntity).hatchLoaded();
            }
        }
    }

    boolean isUnlinked() {
        return controllerPos == null;
    }

    void unlink() {
        controllerPos = null;
        markDirty();
    }

    void link(MultiblockMachineBlockEntity controller) {
        controllerPos = controller.getPos();
        markDirty();
    }

    // TODO: override methods

    @Override
    public void tick() {
        if (world.isClient)
            return;
        lateLoad();
        if (controllerPos != lastSyncedControllerPos)
            sync();
        if (extractItems && type == ITEM_OUTPUT) {
            inventory.autoExtractItems(world, pos, outputDirection);
        }
        if (extractFluids && type == FLUID_OUTPUT) {
            inventory.autoExtractFluids(world, pos, outputDirection);
        }
        if (extractItems && type == ITEM_INPUT) {
            Storage<ItemKey> source = ItemApi.SIDED.get(world, pos.offset(outputDirection), outputDirection.getOpposite());
            if (source != null) {
                Movement.move(source, inventory.itemStorage, key -> true, Integer.MAX_VALUE);
            }
        }
        if (extractFluids && type == FLUID_INPUT) {
            Storage<Fluid> source = FluidApi.SIDED.get(world, pos.offset(outputDirection), outputDirection.getOpposite());
            if (source != null) {
                Movement.move(source, inventory.fluidStorage, key -> true, Integer.MAX_VALUE);
            }
        }
        markDirty();
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        NbtHelper.putBlockPos(tag, "controllerPos", controllerPos);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        controllerPos = NbtHelper.getBlockPos(tag, "controllerPos");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        NbtHelper.putBlockPos(tag, "controllerPos", controllerPos);
        lastSyncedControllerPos = controllerPos;
        return super.toClientTag(tag);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        controllerPos = NbtHelper.getBlockPos(tag, "controllerPos");
        if (controllerPos == null) {
            controllerPos = null;
            casingOverride = null;
        } else {
            MultiblockMachineBlockEntity be = (MultiblockMachineBlockEntity) world.getBlockEntity(controllerPos);
            casingOverride = be == null ? null : be.hatchCasing;
        }
        super.fromClientTag(tag);
    }

    @Override
    public void markRemoved() {
        if (controllerPos != null) {
            MultiblockMachineBlockEntity be = (MultiblockMachineBlockEntity) world.getBlockEntity(controllerPos);
            if (be != null) {
                be.hatchRemoved(pos);
            }
        }
        super.markRemoved();
    }
}
