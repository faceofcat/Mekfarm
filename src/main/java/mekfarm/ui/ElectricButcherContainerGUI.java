package mekfarm.ui;

import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherContainerGUI extends FarmContainerGUI {
    public ElectricButcherContainerGUI(TileEntity tileEntity, Container container) {
        super(tileEntity, container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        mc.getTextureManager().bindTexture(FarmContainerGUI.MACHINE_BACKGROUND);
        drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 5, 117, 208, 18, 18);
        drawTexturedModalRect(super.guiLeft + 153, super.guiTop + 5, 117, 208, 18, 18);
    }
}
