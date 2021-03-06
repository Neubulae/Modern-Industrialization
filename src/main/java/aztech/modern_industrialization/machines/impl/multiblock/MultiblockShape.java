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

import static net.minecraft.util.math.Direction.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * A multiblock shape. It uses its own coordinate system. The controller block
 * is placed at (0, 0, 0), facing north (-z).
 */
public class MultiblockShape {
    public interface Entry {
        boolean matches(BlockView world, BlockPos pos);

        Text getErrorMessage();

        BlockState getPreviewState();

        default boolean allowsHatch(HatchType type) {
            return false;
        }
    }

    Map<BlockPos, Entry> entries = new HashMap<>();
    int maxHatches = Integer.MAX_VALUE;
    private Text errorMessage = null;

    public void addEntry(BlockPos pos, Entry entry) {
        if (entry == null)
            throw new IllegalArgumentException("Can't accept null entry");
        if (entries.put(pos, entry) != null)
            throw new IllegalStateException("Can't override an existing multiblock entry");
    }

    public void addEntry(int x, int y, int z, Entry entry) {
        addEntry(new BlockPos(x, y, z), entry);
    }

    public MultiblockShape setMaxHatches(int maxHatches) {
        this.maxHatches = maxHatches;
        return this;
    }

    public boolean matchShape(World world, BlockPos controllerPos, Direction controllerDirection, Map<BlockPos, HatchBlockEntity> outHatches,
            Set<BlockPos> outStructure) {
        if (controllerDirection.getAxis().isVertical())
            throw new IllegalArgumentException("Multiblocks can only be oriented horizontally");
        errorMessage = null;

        int hatches = 0;
        for (Map.Entry<BlockPos, Entry> entry : entries.entrySet()) {
            BlockPos worldPos = MultiblockShapes.toWorldPos(entry.getKey(), controllerDirection, controllerPos);
            if (entry.getValue().matches(world, worldPos)) {
                if (world.getBlockEntity(worldPos) instanceof HatchBlockEntity) {
                    ++hatches;
                    outHatches.put(worldPos, (HatchBlockEntity) world.getBlockEntity(worldPos));
                } else {
                    outStructure.add(worldPos);
                }
            } else {
                errorMessage = new TranslatableText("text.modern_industrialization.shape_error", worldPos.getX(), worldPos.getY(), worldPos.getZ(),
                        entry.getValue().getErrorMessage());
                return false;
            }
        }

        if (hatches > maxHatches) {
            errorMessage = new TranslatableText("text.modern_industrialization.shape_error_too_many_hatches", hatches, maxHatches);
            return false;
        }

        errorMessage = new TranslatableText("text.modern_industrialization.shape_valid");
        return true;
    }

    public Text getErrorMessage() {
        return errorMessage;
    }
}
