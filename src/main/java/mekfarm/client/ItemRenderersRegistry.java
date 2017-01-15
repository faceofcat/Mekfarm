package mekfarm.client;

import mekfarm.common.ItemsRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2016-10-26.
 */
public final class ItemRenderersRegistry {
    @SideOnly(Side.CLIENT)
    public static final void registerItemRenderers() {
        ItemRenderersRegistry.registerItemRenderer(ItemsRegistry.animalPackage);
        ItemRenderersRegistry.registerItemRenderer(ItemsRegistry.animalFilter);
        ItemRenderersRegistry.registerItemRenderer(ItemsRegistry.animalAgeBabyFilter);
        ItemRenderersRegistry.registerItemRenderer(ItemsRegistry.animalAgeAdultFilter);
    }

    private static void registerItemRenderer(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
