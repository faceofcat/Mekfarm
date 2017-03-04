package mekfarm.common;

import mekfarm.fluids.SewageFluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by CF on 2017-02-25.
 */
public final class FluidsRegistry {
    public static SewageFluid sewage;

    public static final void createFluids() {
        FluidRegistry.enableUniversalBucket();
        (FluidsRegistry.sewage = new SewageFluid()).register();
    }
}
