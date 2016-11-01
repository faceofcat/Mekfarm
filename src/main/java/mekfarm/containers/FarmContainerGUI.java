package mekfarm.containers;

import mekfarm.MekfarmMod;
import mekfarm.entities.FarmTileEntity;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainerGUI extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(MekfarmMod.MODID, "textures/gui/animal_farmer.png");

    private FarmTileEntity te;

    public FarmContainerGUI(FarmTileEntity tileEntity, FarmContainer container) {
        super(container);

        super.xSize = WIDTH;
        super.ySize = HEIGHT;

        this.te = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Animal Farm", 28, 8, 4210751);
        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
        if (tesla != null) {
            String power = TeslaUtils.getDisplayableTeslaCount(tesla.getStoredPower());
            String max = TeslaUtils.getDisplayableTeslaCount(tesla.getCapacity());
            GL11.glPushMatrix();
            try {
                GL11.glScalef(.5f, .5f, .5f);
                fontRendererObj.drawString(power, Math.round(28 / .5f), Math.round((55 - fontRendererObj.FONT_HEIGHT) / .5f), 4210751);
                fontRendererObj.drawString("/ " + max, Math.round(28 / .5f), Math.round((55 - fontRendererObj.FONT_HEIGHT / 2) / .5f), 4210751);
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

        drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 23, 180, 0, Math.round(54.0f * this.te.getWorkProgress()), 5);

        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        if (tesla != null) {
            // MekfarmMod.logger.info("drawing tesla: " + tesla.getStoredPower() + " / " + tesla.getCapacity());
            PowerBar bar = new PowerBar(this, super.guiLeft + 11, super.guiTop + 7, PowerBar.BackgroundType.LIGHT);
            bar.draw(tesla);
        }
    }
}
