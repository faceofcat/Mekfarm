package mekfarm.client;

import mekfarm.common.BlocksRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2016-10-26.
 */
final class BlockRendererRegistry {
    @SideOnly(Side.CLIENT)
    static void registerBlockRenderers() {
        BlocksRegistry.animalFarmBlock.registerRenderer();
        BlocksRegistry.animalReleaserBlock.registerRenderer();
        BlocksRegistry.electricButcherBlock.registerRenderer();
        BlocksRegistry.cropFarmBlock.registerRenderer();
    }
}
