package mekfarm.common;

import mekfarm.fluids.LiquidXPFluid;
import mekfarm.fluids.SewageFluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by CF on 2017-02-25.
 */
public final class FluidsRegistry {
    public static SewageFluid sewage;
    public static LiquidXPFluid liquidXP;

    public static final void createFluids() {
        FluidRegistry.enableUniversalBucket();

        FluidsRegistry.sewage = new SewageFluid();
        FluidsRegistry.sewage.register();

        (FluidsRegistry.liquidXP = new LiquidXPFluid()).register();

//        Fluid liquidXP = null;
////        for(Fluid f : FluidRegistry.getRegisteredFluids().values()) {
////            if (Objects.equals(f.getUnlocalizedName(), "eio.xpjuice")) {
////                liquidXP = f;
////                break;
////            }
////        }
//        if (null == (FluidsRegistry.liquidXP = liquidXP)) {
//            FluidsRegistry.liquidXP = new Fluid("liquidxp",
//                    new ResourceLocation(MekfarmMod.MODID, "blocks/liquidxp_still"),
//                    new ResourceLocation(MekfarmMod.MODID, "blocks/liquidxp_flow"))
//                    .setLuminosity(10)
//                    .setDensity(800)
//                    .setViscosity(1500);
//            FluidRegistry.registerFluid(FluidsRegistry.liquidXP);
//            FluidRegistry.addBucketForFluid(FluidsRegistry.liquidXP);
//        }
    }
}
