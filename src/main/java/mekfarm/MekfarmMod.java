package mekfarm;

import mekfarm.common.CommonProxy;
import mekfarm.net.IMekfarmPackets;
import mekfarm.net.MekfarmPackets;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = MekfarmMod.MODID, version = MekfarmMod.VERSION, name = "Mekfarm", dependencies = "after:Mekanism", useMetadata = true)
public class MekfarmMod
{
    public static final String MODID = "mekfarm";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "mekfarm.client.ClientProxy", serverSide = "mekfarm.common.CommonProxy")
    private static CommonProxy proxy;

    @Mod.Instance
    public static MekfarmMod instance;

    public static Logger logger;

    public static IMekfarmPackets network = new MekfarmPackets();

    public static Side getSide() {
        return MekfarmMod.proxy.getSide();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MekfarmMod.logger = event.getModLog();
        MekfarmMod.logger.info("MekfarmMod::preInit");

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MekfarmMod.logger.info("MekfarmMod::init");
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        MekfarmMod.logger.info("MekfarmMod::postInit");
        proxy.postInit(e);
    }
}
