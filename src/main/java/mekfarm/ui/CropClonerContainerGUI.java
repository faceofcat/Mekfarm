package mekfarm.ui;

import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 * Created by CF on 2016-11-18.
 */
public class CropClonerContainerGUI extends FarmContainerGUI {
    public CropClonerContainerGUI(TileEntity tileEntity, Container container) {
        super(tileEntity, container);
    }

    @Override
    public void initGui() {
        super.initGui();

        if (this.te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            super.addMekUIElement(new FluidUIElement(super.te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), this, 45, 23));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        mc.getTextureManager().bindTexture(FarmContainerGUI.MACHINE_BACKGROUND);
        drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 5, 117, 208, 18, 18);
        drawTexturedModalRect(super.guiLeft + 153, super.guiTop + 5, 117, 208, 18, 18);
    }
}
