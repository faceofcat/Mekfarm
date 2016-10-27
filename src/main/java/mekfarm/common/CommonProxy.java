package mekfarm.common;

import mekfarm.MekfarmMod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by CF on 2016-10-26.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::preInit");

        BlocksRegistry.createBlocks();
        ItemsRegistry.createItems();
    }

    public void init(FMLInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::init");
    }

    public void postInit(FMLPostInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::postInit");
    }
}
