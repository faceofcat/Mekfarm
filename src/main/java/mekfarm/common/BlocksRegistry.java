package mekfarm.common;

import mekfarm.MekfarmMod;
import mekfarm.blocks.FarmBlock;
import mekfarm.entities.FarmTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static Block farmBlock;

    public static final void createBlocks() {
        GameRegistry.register(BlocksRegistry.farmBlock = new FarmBlock());
        GameRegistry.register(new ItemBlock(BlocksRegistry.farmBlock), BlocksRegistry.farmBlock.getRegistryName());
        GameRegistry.registerTileEntity(FarmTileEntity.class, MekfarmMod.MODID + "_farm");
        MekfarmMod.logger.info("Registered block: " + BlocksRegistry.farmBlock.getRegistryName().toString() + ".");
    }
}
