package mekfarm.client;

import mekfarm.MekfarmMod;
import mekfarm.common.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2016-10-26.
 */
public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    public static final ResourceLocation MACHINES_BACKGROUND = new ResourceLocation(MekfarmMod.MODID, "textures/gui/machines.png");

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MekfarmMod.logger.info("ClientProxy::preInit");

        OBJLoader.INSTANCE.addDomain(MekfarmMod.MODID);

        // Typically initialization of models and such goes here:
        BlockRendererRegistry.registerBlockRenderers();
        ItemRenderersRegistry.registerItemRenderers();
        FluidRenderersRegistry.registerFluidRenderers();
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }
}
