package mekfarm;

import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.CommonProxy;
import mekfarm.common.FakeMekPlayer;
import mekfarm.common.ItemsRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = MekfarmMod.MODID, version = MekfarmMod.VERSION, name = "Mekfarm", dependencies = "after:teslacorelib;after:EnderIO", useMetadata = true)
public class MekfarmMod
{
    public static final String MODID = "mekfarm";
    public static final String VERSION = "0.0.1";

    @SidedProxy(clientSide = "mekfarm.client.ClientProxy", serverSide = "mekfarm.common.CommonProxy")
    private static CommonProxy proxy;

    @Mod.Instance
    public static MekfarmMod instance;

    public static MekfarmConfig config;

    public static Logger logger;

//    public static ITeslaCorePackets network = new TeslaCorePackets(MODID);

    public static CreativeTabs creativeTab = new CreativeTabs("tabMekfarm") {
        @Override
        public ItemStack getIconItemStack()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("hasAnimal", 1);
            return new ItemStack(ItemsRegistry.animalPackage, 1, 0, compound);
        }

        @Override
        public Item getTabIconItem() { return this.getIconItemStack().getItem(); }
    };

    private static HashMap<String, FakeMekPlayer> fakePlayers = new HashMap<>();

    public static FakeMekPlayer getFakePlayer(World world) {
        String key = ((world != null) && (world.provider != null))
                ? String.format("%d", world.provider.getDimension())
                : null;
        if (key != null) {
            if (fakePlayers.containsKey(key)) {
                return fakePlayers.get(key);
            }

            if (world instanceof WorldServer) {
                FakeMekPlayer player = new FakeMekPlayer((WorldServer) world);
//                BlockPos spawn = world.getSpawnPoint();
//                if (spawn == null) {
//                    spawn = new BlockPos(0, 0, 0);
//                }
//                player.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
                fakePlayers.put(key, player);
                return player;
            }
        }
        return null;
    }

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MekfarmMod.config = new MekfarmConfig(event.getSuggestedConfigurationFile());

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
