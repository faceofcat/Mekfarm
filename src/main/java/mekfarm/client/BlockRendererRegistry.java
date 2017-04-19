package mekfarm.client;

import mekfarm.common.BlocksRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2016-10-26.
 */
final class BlockRendererRegistry {
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("ConstantConditions")
    static void registerBlockRenderers() {
        BlocksRegistry.animalFarmBlock.registerRenderer();
        BlocksRegistry.animalReleaserBlock.registerRenderer();
        BlocksRegistry.electricButcherBlock.registerRenderer();
        BlocksRegistry.cropFarmBlock.registerRenderer();
        BlocksRegistry.cropClonerBlock.registerRenderer();
        BlocksRegistry.animalGymBlock.registerRenderer();
        BlocksRegistry.treeFarmBlock.registerRenderer();
        BlocksRegistry.sewerBlock.registerRenderer();
        BlocksRegistry.xpStorageBlock.registerRenderer();

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlocksRegistry.dungBlock)
                , 0
                , new ModelResourceLocation(BlocksRegistry.dungBlock.getRegistryName(), "inventory")
        );
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlocksRegistry.dungBricks)
                , 0
                , new ModelResourceLocation(BlocksRegistry.dungBricks.getRegistryName(), "inventory")
        );
    }
}
