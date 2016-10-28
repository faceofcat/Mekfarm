package mekfarm.client;

import mekfarm.MekfarmMod;
import mekfarm.common.CommonProxy;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by CF on 2016-10-26.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MekfarmMod.logger.info("ClientProxy::preInit");

        OBJLoader.INSTANCE.addDomain(MekfarmMod.MODID);

        // Typically initialization of models and such goes here:
        BlockRendererRegistry.registerBlockRenderers();
        ItemRenderersRegistry.registerItemRenderers();
    }
}
