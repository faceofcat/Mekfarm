package mekfarm.containers;

import mekfarm.MekfarmMod;
import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.IWorkProgress;
import mekfarm.farms.AnimalFarmEntity;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainerGUI extends GuiContainer {
    private static final ResourceLocation background = new ResourceLocation(MekfarmMod.MODID, "textures/gui/animal_farmer.png");

    private TileEntity te;

    @SuppressWarnings("unused") // used from reflection
    public FarmContainerGUI(TileEntity tileEntity, Container container) {
        super(container);

        super.xSize = tileEntity.hasCapability(MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER, null) ? 210 : 180;
        super.ySize = 170;

        this.te = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Animal Farm", 8, 8, 4210751);
        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (tesla != null) {
            String power = TeslaUtils.getDisplayableTeslaCount(tesla.getStoredPower());
            String max = TeslaUtils.getDisplayableTeslaCount(tesla.getCapacity());
            GL11.glPushMatrix();
            try {
                GL11.glScalef(.5f, .5f, .5f);
                fontRendererObj.drawString(power, Math.round(28 / .5f), Math.round((73 - fontRendererObj.FONT_HEIGHT) / .5f), 4210751);
                fontRendererObj.drawString("/ " + max, Math.round(28 / .5f), Math.round((73 - fontRendererObj.FONT_HEIGHT / 2) / .5f), 4210751);
            }
            finally {
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(super.guiLeft, super.guiTop, 0, 0, super.xSize, super.ySize);

        if (this.te instanceof IWorkProgress) {
            drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 23, 0, 170, Math.round(54.0f * ((IWorkProgress)this.te).getWorkProgress()), 5);
        }

        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        if (tesla != null) {
            // MekfarmMod.logger.info("drawing tesla: " + tesla.getStoredPower() + " / " + tesla.getCapacity());
            PowerBar bar = new PowerBar(this, super.guiLeft + 11, super.guiTop + 25, PowerBar.BackgroundType.LIGHT);
            bar.draw(tesla);
        }
    }
}
