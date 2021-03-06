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
package aztech.modern_industrialization.machines.special;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.api.FluidFuelRegistry;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.MIMachines;
import aztech.modern_industrialization.machines.impl.MachineFactory;
import aztech.modern_industrialization.machines.impl.multiblock.MultiblockMachineBlockEntity;
import aztech.modern_industrialization.machines.impl.multiblock.MultiblockShape;
import aztech.modern_industrialization.util.ItemStackHelper;
import java.util.List;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.math.Direction;

public class LargeSteamBoilerBlockEntity extends MultiblockMachineBlockEntity {
    private static final int EU_PRODUCTION = 256;

    public LargeSteamBoilerBlockEntity(MachineFactory factory, List<MultiblockShape> shapes) {
        super(factory, shapes);

        maxEfficiencyTicks = 10000;
        efficiencyTicks = 0;
        hatchCasing = MIMachines.ELECTRIC_BLAST_FURNACE.machineModel;
    }

    @Override
    public void tick() {
        if (world.isClient)
            return;

        this.tickCheckShape();

        boolean wasActive = isActive;

        this.isActive = false;
        if (ready) {
            // Item fuels
            if (usedEnergy == 0) {
                for (ConfigurableItemStack stack : getItemInputStacks()) {
                    Item fuel = stack.getItemKey().getItem();
                    if (ItemStackHelper.consumeFuel(stack, true)) {
                        Integer fuelTime = FuelRegistryImpl.INSTANCE.get(fuel);
                        if (fuelTime != null && fuelTime > 0) {
                            recipeEnergy = fuelTime / 8;
                            usedEnergy = recipeEnergy;
                            ItemStackHelper.consumeFuel(stack, false);
                            break;
                        }
                    }
                }
            }
            // Fluid fuels
            if (usedEnergy == 0) {
                for (ConfigurableFluidStack stack : getFluidInputStacks()) {
                    Fluid fluid = stack.getFluid();
                    int burnTicks = FluidFuelRegistry.getEu(fluid);
                    if (burnTicks > 0) {
                        int mbEu = FluidFuelRegistry.getEu(fluid) * 2;
                        int necessaryAmount = Math.max(1, EU_PRODUCTION / mbEu);
                        if (stack.getAmount() / 81 >= necessaryAmount) {
                            recipeEnergy = mbEu * necessaryAmount / EU_PRODUCTION;
                            usedEnergy = recipeEnergy;
                            stack.decrement(necessaryAmount * 81);
                            break;
                        }
                    }
                }
            }

            if (usedEnergy > 0) {
                isActive = true;
                --usedEnergy;
            }
        }

        if (isActive) {
            efficiencyTicks = Math.min(efficiencyTicks + 1, maxEfficiencyTicks);
        } else {
            efficiencyTicks = Math.max(efficiencyTicks - 1, 0);
        }

        if (ready) {
            if (efficiencyTicks > 1000) {
                int steamProduction = 81 * EU_PRODUCTION * efficiencyTicks / maxEfficiencyTicks;
                boolean waterAvailable = false;
                // Check if there is some water available
                for (ConfigurableFluidStack fluidStack : getFluidInputStacks()) {
                    if (fluidStack.getFluid() == Fluids.WATER && fluidStack.getAmount() > 0) {
                        waterAvailable = true;
                        break;
                    }
                }
                if (waterAvailable) {
                    boolean producedSteam = false;
                    // Try to output some steam
                    for (ConfigurableFluidStack fluidStack : getFluidOutputStacks()) {
                        if (fluidStack.isValid(MIFluids.STEAM)) {
                            long ins = Math.min(steamProduction, fluidStack.getRemainingSpace());
                            if (ins > 0) {
                                fluidStack.setFluid(MIFluids.STEAM);
                                fluidStack.increment(ins);
                                producedSteam = true;
                                steamProduction -= ins;
                            }
                        }
                    }

                    if (producedSteam) {
                        for (ConfigurableFluidStack fluidStack : getFluidInputStacks()) {
                            if (fluidStack.getFluid() == Fluids.WATER && fluidStack.getAmount() > 0) {
                                fluidStack.decrement(1);
                            }
                        }
                    }
                }
            }
        }

        if (isActive != wasActive) {
            sync();
        }
        markDirty();

        for (Direction direction : Direction.values()) {
            inventory.autoExtractFluids(world, pos, direction);
        }
    }

    @Override
    public boolean hasOutput() {
        return false;
    }
}
