package mekfarm.common;

import mekfarm.MekfarmMod;
import mekfarm.blocks.FarmBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static Block farmBlock;

    public static final void createBlocks() {
        GameRegistry.register(BlocksRegistry.farmBlock = new FarmBlock());
        MekfarmMod.logger.info("Registered block: " + BlocksRegistry.farmBlock.getRegistryName().toString() + ".");

        GameRegistry.register(new ItemBlock(BlocksRegistry.farmBlock), BlocksRegistry.farmBlock.getRegistryName());
        MekfarmMod.logger.info("Registered item block: " + BlocksRegistry.farmBlock.getRegistryName().toString() + ".");
    }
}
