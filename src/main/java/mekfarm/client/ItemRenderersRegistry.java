package mekfarm.client;

import mekfarm.common.ItemsRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2016-10-26.
 */
public final class ItemRenderersRegistry {
    @SideOnly(Side.CLIENT)
    public static final void registerItemRenderers() {
        ModelLoader.setCustomModelResourceLocation(
                ItemsRegistry.farmItem,
                0,
                new ModelResourceLocation(ItemsRegistry.farmItem.getRegistryName(), "inventory")
        );
        ModelLoader.setCustomModelResourceLocation(
                ItemsRegistry.animalPackage,
                0,
                new ModelResourceLocation(ItemsRegistry.animalPackage.getRegistryName(), "inventory")
        );
    }
}
