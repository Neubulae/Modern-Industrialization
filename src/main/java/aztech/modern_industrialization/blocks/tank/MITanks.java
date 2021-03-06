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
package aztech.modern_industrialization.blocks.tank;

import static aztech.modern_industrialization.ModernIndustrialization.ITEM_GROUP;

import aztech.modern_industrialization.MIIdentifier;
import aztech.modern_industrialization.ModernIndustrialization;
import aztech.modern_industrialization.blocks.creativetank.CreativeTankBlock;
import aztech.modern_industrialization.blocks.creativetank.CreativeTankBlockEntity;
import aztech.modern_industrialization.blocks.creativetank.CreativeTankItem;
import aztech.modern_industrialization.blocks.creativetank.CreativeTankRenderer;
import aztech.modern_industrialization.model.block.ModelProvider;
import java.util.Arrays;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidApi;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public enum MITanks {
    BRONZE("bronze", 4),
    STEEL("steel", 8),
    ALUMINUM("aluminum", 16),
    STAINLESS_STEEL("stainless_steel", 32);

    public static BlockEntityType<TankBlockEntity> BLOCK_ENTITY_TYPE;
    public static BlockEntityType<CreativeTankBlockEntity> CREATIVE_BLOCK_ENTITY_TYPE;
    public static CreativeTankBlock CREATIVE_TANK_BLOCK = new CreativeTankBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f));
    public static CreativeTankItem CREATIVE_TANK_ITEM = new CreativeTankItem(CREATIVE_TANK_BLOCK, new Item.Settings().group(ITEM_GROUP));

    public final String type;
    public final TankBlock block;
    public final TankItem item;
    public final int bucketCapacity;

    MITanks(String type, int bucketCapacity) {
        this.type = type;
        this.block = new TankBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f));
        this.item = new TankItem(block, new Item.Settings().group(ITEM_GROUP), 81000 * bucketCapacity);
        this.bucketCapacity = bucketCapacity;
    }

    public static void setup() {
        ModernIndustrialization.registerBlock(CREATIVE_TANK_BLOCK, CREATIVE_TANK_ITEM, "creative_tank");
        for (MITanks tank : values()) {
            ModernIndustrialization.registerBlock(tank.block, tank.item, "tank_" + tank.type, 0);
        }
        BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new MIIdentifier("tank"),
                BlockEntityType.Builder.create(TankBlockEntity::new, getBlocks()).build(null));
        CREATIVE_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new MIIdentifier("creative_tank"),
                BlockEntityType.Builder.create(CreativeTankBlockEntity::new, CREATIVE_TANK_BLOCK).build(null));

        // Fluid API
        FluidApi.SIDED.registerForBlockEntities((be, direction) -> be instanceof TankBlockEntity ? (TankBlockEntity) be : null, BLOCK_ENTITY_TYPE);
        FluidApi.SIDED.registerForBlockEntities((be, direction) -> be instanceof CreativeTankBlockEntity ? (CreativeTankBlockEntity) be : null,
                CREATIVE_BLOCK_ENTITY_TYPE);
        for (MITanks tank : values()) {
            tank.item.registerItemApi();
        }
        FluidApi.ITEM.register(CreativeTankItem.TankItemStorage::new, CREATIVE_TANK_ITEM);
    }

    public static void setupClient() {
        for (MITanks tank : values()) {
            UnbakedModel tankModel = new TankModel(tank.type);
            ModelProvider.modelMap.put(new MIIdentifier("block/tank_" + tank.type), tankModel);
            ModelProvider.modelMap.put(new MIIdentifier("item/tank_" + tank.type), tankModel);
        }
        UnbakedModel creativeTankModel = new TankModel("creative");
        ModelProvider.modelMap.put(new MIIdentifier("block/creative_tank"), creativeTankModel);
        ModelProvider.modelMap.put(new MIIdentifier("item/creative_tank"), creativeTankModel);

        BlockEntityRendererRegistry.INSTANCE.register(BLOCK_ENTITY_TYPE, TankRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(CREATIVE_BLOCK_ENTITY_TYPE, CreativeTankRenderer::new);
    }

    private static Block[] getBlocks() {
        return Arrays.stream(values()).map(x -> x.block).toArray(Block[]::new);
    }
}
