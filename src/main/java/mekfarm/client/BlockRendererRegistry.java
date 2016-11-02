package mekfarm.client;

import mekfarm.MekfarmMod;
import mekfarm.common.BlocksRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlockRendererRegistry {
    @SideOnly(Side.CLIENT)
    public static final void registerBlockRenderers() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlocksRegistry.farmBlock), 0, new ModelResourceLocation(BlocksRegistry.farmBlock.getRegistryName(), "inventory"));
    }
}
