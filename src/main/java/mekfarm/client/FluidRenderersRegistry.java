package mekfarm.client;

import mekfarm.common.FluidsRegistry;

/**
 * Created by CF on 2017-02-25.
 */
public final class FluidRenderersRegistry {
    public static void registerFluidRenderers() {
        FluidsRegistry.sewage.registerRenderer();
        FluidsRegistry.liquidXP.registerRenderer();
    }
}
