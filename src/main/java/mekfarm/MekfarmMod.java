package mekfarm;

import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.CommonProxy;
import mekfarm.common.ItemsRegistry;
import mekfarm.net.IMekfarmPackets;
import mekfarm.net.MekfarmPackets;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MekfarmMod.MODID, version = MekfarmMod.VERSION, name = "Mekfarm", dependencies = "after:tesla", useMetadata = true)
public class MekfarmMod
{
    public static final String MODID = "mekfarm";
    public static final String VERSION = "0.0.1";

    @SidedProxy(clientSide = "mekfarm.client.ClientProxy", serverSide = "mekfarm.common.CommonProxy")
    private static CommonProxy proxy;

    @Mod.Instance
    public static MekfarmMod instance;

    public static Logger logger;

    public static IMekfarmPackets network = new MekfarmPackets();

    public static CreativeTabs creativeTab = new CreativeTabs("tabMekfarm") {
        @Override
        public ItemStack getIconItemStack()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("hasAnimal", 1);
            return new ItemStack(ItemsRegistry.animalPackage, 1, 0, compound);
        }

        @Override
        public Item getTabIconItem()
        {
            return ItemsRegistry.animalPackage;
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MekfarmMod.logger = event.getModLog();
        MekfarmMod.logger.info("MekfarmMod::preInit");

        proxy.preInit(event);
        MekfarmCapabilities.register();
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
