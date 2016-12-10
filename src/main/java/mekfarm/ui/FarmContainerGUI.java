package mekfarm.ui;

import mekfarm.capabilities.IMachineInfo;
import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.containers.TexturedSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.ndrei.teslacorelib.tileentity.IWorkProgressProvider;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainerGUI extends MekUIContainer {
    @SuppressWarnings("unused") // used from reflection
    public FarmContainerGUI(TileEntity tileEntity, Container container) {
        super(tileEntity, container);

        super.xSize = tileEntity.hasCapability(MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER, null) ? 210 : 180;
        super.ySize = 170;
    }

    @Override
    public void initGui() {
        super.initGui();

        super.addMekUIElement(new EnergyUIElement(super.te, this, 9, 23));
    }

    @Override
    protected void drawGuiContainerForeground(int mouseX, int mouseY) {
        super.drawGuiContainerForeground(mouseX, mouseY);
        if (this.te.hasCapability(MekfarmCapabilities.CAPABILITY_MACHINE_INFO, null)) {
            IMachineInfo info = this.te.getCapability(MekfarmCapabilities.CAPABILITY_MACHINE_INFO, null);
            if (info != null) {
                String title = info.getUnlocalizedMachineName();
                title = I18n.format(title);
                fontRendererObj.drawString(title, 8, 8, 4210751);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.bindDefaultTexture();
        this.drawTexturedModalRect(super.guiLeft, super.guiTop, 0, 0, super.xSize, super.ySize);
        this.drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 5, 182, 66, 54, 72);

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        this.bindDefaultTexture();
        this.inventorySlots.inventorySlots
                .stream()
                .filter(slot -> slot instanceof TexturedSlot)
                .forEach(slot -> ((TexturedSlot) slot).draw(super.guiLeft, super.guiTop, this));

        if (this.te.hasCapability(MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER, null)) {
            this.drawTexturedModalRect(super.guiLeft + 187, super.guiTop + 5, 212, 5, 18, 54);
        }

        if (super.te instanceof IWorkProgressProvider) {
            drawTexturedModalRect(super.guiLeft + 117, super.guiTop + 23, 0, 170, Math.round(54.0f * ((IWorkProgressProvider)this.te).getJobProgress()), 5);
        }
    }
}
