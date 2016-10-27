package mekfarm.client;

import mekfarm.MekfarmMod;
import mekfarm.common.CommonProxy;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by CF on 2016-10-26.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MekfarmMod.logger.info("ClientProxy::init");

        OBJLoader.INSTANCE.addDomain(MekfarmMod.MODID);

        // Typically initialization of models and such goes here:
        BlockRendererRegistry.registerBlockRenderers();
        ItemRenderersRegistry.registerItemRenderers();
    }
}
