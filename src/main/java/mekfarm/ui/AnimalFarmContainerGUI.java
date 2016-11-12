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

    private int drawInputSlot1 = 0;
    private int drawInputSlot2 = 1;

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
            this.drawInputSlot1 = (this.drawInputSlot1 + 1) % 4;
        }
        if (this.uiFakeTick2++ > FAKE_TICKS) {
            this.uiFakeTick2 = 0;
            this.drawInputSlot2 = (this.drawInputSlot2 + 1) % 4;
        }

        super.drawTexturedModalRect(super.guiLeft + 135, super.guiTop + 5, 135 + this.drawInputSlot1 * 18, 172, 18, 18);
        super.drawTexturedModalRect(super.guiLeft + 153, super.guiTop + 5, 135 + this.drawInputSlot2 * 18, 172, 18, 18);
    }
}
