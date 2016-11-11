package mekfarm.common;

import mekfarm.MekfarmMod;
import mekfarm.ui.MekfarmGuiProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

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

        NetworkRegistry.INSTANCE.registerGuiHandler(MekfarmMod.instance, new MekfarmGuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::postInit");
    }

    public Side getSide() {
        return Side.SERVER;
    }
}
