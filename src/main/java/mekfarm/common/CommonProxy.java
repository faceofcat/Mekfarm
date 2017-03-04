package mekfarm.common;

import mekfarm.MekfarmMod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by CF on 2016-10-26.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::preInit");

        FluidsRegistry.createFluids();
        ItemsRegistry.createItems();
        BlocksRegistry.createBlocks();
    }

    public void init(FMLInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::init");

        // NetworkRegistry.INSTANCE.registerGuiHandler(MekfarmMod.instance, new MekfarmGuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        MekfarmMod.logger.info("CommonProxy::postInit");
    }

    public Side getSide() {
        return Side.SERVER;
    }
}
