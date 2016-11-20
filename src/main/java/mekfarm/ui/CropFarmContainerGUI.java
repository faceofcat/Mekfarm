package mekfarm.ui;

import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 * Created by CF on 2016-11-18.
 */
public class CropFarmContainerGUI extends FarmContainerGUI {
    public CropFarmContainerGUI(TileEntity tileEntity, Container container) {
        super(tileEntity, container);
    }

    @Override
    public void initGui() {
        super.initGui();

        if (this.te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            super.addMekUIElement(new FluidUIElement(super.te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), this, 45, 23));
        }
    }
}
