
package aztech.modern_industrialization;

import aztech.modern_industrialization.fluid.CraftingFluid;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.*;
import net.minecraft.util.registry.Registry;

import static aztech.modern_industrialization.ModernIndustrialization.MOD_ID;
import static aztech.modern_industrialization.ModernIndustrialization.RESOURCE_PACK;

/**
 * This is auto-generated, don't edit by hand!
 */
public class MIFluids {
    public static final CraftingFluid ACETYLENE = new CraftingFluid("acetylene", 0xff603405);
    public static final CraftingFluid ACRYLIC_ACID = new CraftingFluid("acrylic_acid", 0xff1bdeb5);
    public static final CraftingFluid ACRYLIC_GLUE = new CraftingFluid("acrylic_glue", 0xff1bde54);
    public static final CraftingFluid AIR = new CraftingFluid("air", 0xff76c7f9);
    public static final CraftingFluid BENZENE = new CraftingFluid("benzene", 0xfff0d179);
    public static final CraftingFluid BERYLLIUM_CHLORIDE = new CraftingFluid("beryllium_chloride", 0xff85B354);
    public static final CraftingFluid BOOSTED_DIESEL = new CraftingFluid("boosted_diesel", 0xfffd9b0a);
    public static final CraftingFluid BUTADIENE = new CraftingFluid("butadiene", 0xffd0bd1a);
    public static final CraftingFluid CAPROLACTAM = new CraftingFluid("caprolactam", 0xff795450);
    public static final CraftingFluid CHLORINE = new CraftingFluid("chlorine", 0xffb7c114);
    public static final CraftingFluid CHROME_HYDROCHLORIC_SOLUTION = new CraftingFluid("chrome_hydrochloric_solution", 0xfffabe73);
    public static final CraftingFluid CRUDE_OIL = new CraftingFluid("crude_oil", 0xff3e3838);
    public static final CraftingFluid DIESEL = new CraftingFluid("diesel", 0xffe9bf2d);
    public static final CraftingFluid DIETHYL_ETHER = new CraftingFluid("diethyl_ether", 0xff8ec837);
    public static final CraftingFluid ETHANOL = new CraftingFluid("ethanol", 0xff608936);
    public static final CraftingFluid ETHYLBENZENE = new CraftingFluid("ethylbenzene", 0xffc4fa57);
    public static final CraftingFluid ETHYLENE = new CraftingFluid("ethylene", 0xff287671);
    public static final CraftingFluid FLUORINE = new CraftingFluid("fluorine", 0xffDBD576);
    public static final CraftingFluid HEAVY_FUEL = new CraftingFluid("heavy_fuel", 0xffffdb46);
    public static final CraftingFluid HYDROCHLORIC_ACID = new CraftingFluid("hydrochloric_acid", 0xff9ebd06);
    public static final CraftingFluid HYDROGEN = new CraftingFluid("hydrogen", 0xff1b4acc);
    public static final CraftingFluid LIGHT_FUEL = new CraftingFluid("light_fuel", 0xffffe946);
    public static final CraftingFluid MANGANESE_SULFURIC_SOLUTION = new CraftingFluid("manganese_sulfuric_solution", 0xffb96c3f);
    public static final CraftingFluid METHANE = new CraftingFluid("methane", 0xffb740d9);
    public static final CraftingFluid NAPHTHA = new CraftingFluid("naphtha", 0xffa5a25e);
    public static final CraftingFluid NYLON = new CraftingFluid("nylon", 0xff986a64);
    public static final CraftingFluid OXYGEN = new CraftingFluid("oxygen", 0xff3296f2);
    public static final CraftingFluid POLYETHYLENE = new CraftingFluid("polyethylene", 0xff639c98);
    public static final CraftingFluid POLYVINYL_CHLORIDE = new CraftingFluid("polyvinyl_chloride", 0xfff6d3ec);
    public static final CraftingFluid PROPENE = new CraftingFluid("propene", 0xff98644c);
    public static final CraftingFluid RAW_SYNTHETIC_OIL = new CraftingFluid("raw_synthetic_oil", 0xff474740);
    public static final CraftingFluid RAW_RUBBER = new CraftingFluid("raw_rubber", 0xff514a4a);
    public static final CraftingFluid RUBBER = new CraftingFluid("rubber", 0xff1a1a1a);
    public static final CraftingFluid SHALE_OIL = new CraftingFluid("shale_oil", 0xff6e7373);
    public static final CraftingFluid SODIUM_HYDROXIDE = new CraftingFluid("sodium_hydroxide", 0xff5071c9);
    public static final CraftingFluid STEAM = new CraftingFluid("steam", 0xffeeeeee);
    public static final CraftingFluid STEAM_CRACKED_NAPHTHA = new CraftingFluid("steam_cracked_naphtha", 0xffd2d0ae);
    public static final CraftingFluid STYRENE = new CraftingFluid("styrene", 0xff9e47f2);
    public static final CraftingFluid STYRENE_BUTADIENE = new CraftingFluid("styrene_butadiene", 0xff9c8040);
    public static final CraftingFluid STYRENE_BUTADIENE_RUBBER = new CraftingFluid("styrene_butadiene_rubber", 0xff423821);
    public static final CraftingFluid SULFURIC_ACID = new CraftingFluid("sulfuric_acid", 0xffe15b00);
    public static final CraftingFluid SULFURIC_CRUDE_OIL = new CraftingFluid("sulfuric_crude_oil", 0xff4b5151);
    public static final CraftingFluid SULFURIC_HEAVY_FUEL = new CraftingFluid("sulfuric_heavy_fuel", 0xfff2cf3c);
    public static final CraftingFluid SULFURIC_LIGHT_FUEL = new CraftingFluid("sulfuric_light_fuel", 0xfff4dd34);
    public static final CraftingFluid SULFURIC_NAPHTHA = new CraftingFluid("sulfuric_naphtha", 0xffa5975e);
    public static final CraftingFluid SYNTHETIC_OIL = new CraftingFluid("synthetic_oil", 0xff1a1a1a);
    public static final CraftingFluid TOLUENE = new CraftingFluid("toluene", 0xff9ce6ed);
    public static final CraftingFluid VINYL_CHLORIDE = new CraftingFluid("vinyl_chloride", 0xffeda7d9);
    public static final CraftingFluid HELIUM = new CraftingFluid("helium", 0xffe6e485);
    public static final CraftingFluid ARGON = new CraftingFluid("argon", 0xffe339a7);
    public static final CraftingFluid HELIUM_3 = new CraftingFluid("helium_3", 0xff83de52);
    public static final CraftingFluid DEUTERIUM = new CraftingFluid("deuterium", 0xff941bcc);
    public static final CraftingFluid TRITIUM = new CraftingFluid("tritium", 0xffcc1b50);
    public static final CraftingFluid HEAVY_WATER = new CraftingFluid("heavy_water", 0xff6e18f0);
    public static final CraftingFluid HEAVY_WATER_STEAM = new CraftingFluid("heavy_water_steam", 0xffd9cfe8);
    public static final CraftingFluid HIGH_PRESSURE_WATER = new CraftingFluid("high_pressure_water", 0xff144cb8);
    public static final CraftingFluid HIGH_PRESSURE_STEAM = new CraftingFluid("high_pressure_steam", 0xff9c9c9c);
    public static final CraftingFluid HIGH_PRESSURE_HEAVY_WATER = new CraftingFluid("high_pressure_heavy_water", 0xff3d0b8a);
    public static final CraftingFluid HIGH_PRESSURE_HEAVY_WATER_STEAM = new CraftingFluid("high_pressure_heavy_water_steam", 0xff6d647a);
    public static final CraftingFluid LEAD_SODIUM_EUTECTIC = new CraftingFluid("lead_sodium_eutectic", 0xff604170);
    public static final CraftingFluid SOLDERING_ALLOY = new CraftingFluid("soldering_alloy", 0xffabc4bf);
    public static final CraftingFluid LUBRICANT = new CraftingFluid("lubricant", 0xffffc400);
    public static final CraftingFluid[] FLUIDS = new CraftingFluid[] {
            ACETYLENE,
            ACRYLIC_ACID,
            ACRYLIC_GLUE,
            AIR,
            BENZENE,
            BERYLLIUM_CHLORIDE,
            BOOSTED_DIESEL,
            BUTADIENE,
            CAPROLACTAM,
            CHLORINE,
            CHROME_HYDROCHLORIC_SOLUTION,
            CRUDE_OIL,
            DIESEL,
            DIETHYL_ETHER,
            ETHANOL,
            ETHYLBENZENE,
            ETHYLENE,
            FLUORINE,
            HEAVY_FUEL,
            HYDROCHLORIC_ACID,
            HYDROGEN,
            LIGHT_FUEL,
            MANGANESE_SULFURIC_SOLUTION,
            METHANE,
            NAPHTHA,
            NYLON,
            OXYGEN,
            POLYETHYLENE,
            POLYVINYL_CHLORIDE,
            PROPENE,
            RAW_SYNTHETIC_OIL,
            RAW_RUBBER,
            RUBBER,
            SHALE_OIL,
            SODIUM_HYDROXIDE,
            STEAM,
            STEAM_CRACKED_NAPHTHA,
            STYRENE,
            STYRENE_BUTADIENE,
            STYRENE_BUTADIENE_RUBBER,
            SULFURIC_ACID,
            SULFURIC_CRUDE_OIL,
            SULFURIC_HEAVY_FUEL,
            SULFURIC_LIGHT_FUEL,
            SULFURIC_NAPHTHA,
            SYNTHETIC_OIL,
            TOLUENE,
            VINYL_CHLORIDE,
            HELIUM,
            ARGON,
            HELIUM_3,
            DEUTERIUM,
            TRITIUM,
            HEAVY_WATER,
            HEAVY_WATER_STEAM,
            HIGH_PRESSURE_WATER,
            HIGH_PRESSURE_STEAM,
            HIGH_PRESSURE_HEAVY_WATER,
            HIGH_PRESSURE_HEAVY_WATER_STEAM,
            LEAD_SODIUM_EUTECTIC,
            SOLDERING_ALLOY,
            LUBRICANT,
    };
    
    public static void setupFluids() {
    
    }
    
    static {
        for(CraftingFluid fluid : FLUIDS) {
            registerFluid(fluid);
        }
    }
    
    private static void registerFluid(CraftingFluid fluid) {
        String id = fluid.name;
        Registry.register(Registry.FLUID, new MIIdentifier(id), fluid);
        Registry.register(Registry.ITEM, new MIIdentifier("bucket_" + id), fluid.getBucketItem());
        RESOURCE_PACK.addModel(JModel.model().parent("minecraft:item/generated").textures(new JTextures().layer0(MOD_ID + ":items/bucket/" + id)), new MIIdentifier("item/bucket_" + id));
    }
}
