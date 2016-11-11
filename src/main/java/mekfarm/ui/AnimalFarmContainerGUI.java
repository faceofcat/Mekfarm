package mekfarm.ui;

import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-11-11.
 */
public class AnimalFarmContainerGUI extends FarmContainerGUI {
    private static final int FAKE_TICKS = 100;

    private int uiFakeTick1 = 0;
    private int uiFakeTick2 = FAKE_TICKS / 2;

    private boolean drawShears1 = false;
    private boolean drawShears2 = false;

    public AnimalFarmContainerGUI(TileEntity tileEntity, Container container) {
        super(tileEntity, container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        mc.getTextureManager().bindTexture(FarmContainerGUI.MACHINE_BACKGROUND);
        super.drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 5, 117, 172, 54, 18);

        if (this.uiFakeTick1++ > FAKE_TICKS) {
            this.uiFakeTick1 = 0;
            this.drawShears1 = !this.drawShears1;
        }
        if (this.uiFakeTick2++ > FAKE_TICKS) {
            this.uiFakeTick2 = 0;
            this.drawShears2 = !this.drawShears2;
        }

        if (this.drawShears1) {
            super.drawTexturedModalRect(super.guiLeft + 136, super.guiTop + 6, 172, 173, 16, 16);
        }
        if (this.drawShears2) {
            super.drawTexturedModalRect(super.guiLeft + 154, super.guiTop + 6, 172, 173, 16, 16);
        }
    }
}
