package mekfarm.ui;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.capabilities.IMachineInfo;
import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.IWorkProgress;
import mekfarm.containers.TexturedSlot;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainerGUI extends GuiContainer {
    protected static final ResourceLocation MACHINE_BACKGROUND = new ResourceLocation(MekfarmMod.MODID, "textures/gui/most_machines.png");

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
        if (this.te.hasCapability(MekfarmCapabilities.CAPABILITY_MACHINE_INFO, null)) {
            IMachineInfo info = this.te.getCapability(MekfarmCapabilities.CAPABILITY_MACHINE_INFO, null);
            if (info != null) {
                String title = info.getUnlocalizedMachineName();
                title = I18n.format(title);
                fontRendererObj.drawString(title, 8, 8, 4210751);
            }
        }

        if (Loader.isModLoaded("tesla")) {
            this.drawTeslaForeground(mouseX, mouseY);
        }
        else if (this.te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage energy = this.te.getCapability(CapabilityEnergy.ENERGY, null);
            String power = String.format("%,d T", energy.getEnergyStored());
            String max = String.format("%,d T", energy.getMaxEnergyStored());
            this.drawPowerStorage(power, max);
        }
    }

    @Optional.Method(modid = "tesla")
    private void drawTeslaForeground(int mouseX, int mouseY) {
        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        if (tesla != null) {
            String power = TeslaUtils.getDisplayableTeslaCount(tesla.getStoredPower());
            String max = TeslaUtils.getDisplayableTeslaCount(tesla.getCapacity());
            this.drawPowerStorage(power, max);

            mouseX -= super.guiLeft;
            mouseY -= super.guiTop;
            if ((mouseX >= 11) && (mouseX <= (11 + PowerBar.WIDTH)) && (mouseY >= 25) && (mouseY <= (25 + PowerBar.HEIGHT))) {
                List<String> lines = Lists.newArrayList();
                lines.add(String.format("%,d T of", tesla.getStoredPower()));
                lines.add(String.format("%,d T", tesla.getCapacity()));
                super.drawHoveringText(lines, mouseX, mouseY);
            }
        }
    }

    private void drawPowerStorage(String power, String max) {
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(MACHINE_BACKGROUND);
        drawTexturedModalRect(super.guiLeft, super.guiTop, 0, 0, super.xSize, super.ySize);

        this.inventorySlots.inventorySlots
                .stream()
                .filter(slot -> slot instanceof TexturedSlot)
                .forEach(slot -> ((TexturedSlot) slot).draw(super.guiLeft, super.guiTop, this));

        if (this.te.hasCapability(MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER, null)) {
            drawTexturedModalRect(super.guiLeft + 187, super.guiTop + 5, 212, 5, 18, 54);
        }

        if (this.te instanceof IWorkProgress) {
            drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 23, 0, 170, Math.round(54.0f * ((IWorkProgress) this.te).getWorkProgress()), 5);
        }

        if (Loader.isModLoaded("tesla")) {
            this.drawTeslaBackground();
        }
    }

    @Optional.Method(modid = "tesla")
    private void drawTeslaBackground() {
        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        if (tesla != null) {
            // MekfarmMod.logger.info("drawing tesla: " + tesla.getStoredPower() + " / " + tesla.getCapacity());
            PowerBar bar = new PowerBar(this, super.guiLeft + 11, super.guiTop + 25, PowerBar.BackgroundType.LIGHT);
            bar.draw(tesla);
        }
    }
}
