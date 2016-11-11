package mekfarm.containers;

import mekfarm.ui.InternalSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherContainer extends FarmContainer {
    public ElectricButcherContainer(IInventory playerInventory, TileEntity tileEntity) {
        super(playerInventory, tileEntity);
    }

    @Override
    protected void addInputSlots(IItemHandler itemHandler, int inputs) {
        if (inputs != 1) {
            super.addInputSlots(itemHandler, inputs);
        }
        else {
            addSlotToContainer(new InternalSlot(itemHandler, 0, 118 + 18, 6));
        }
    }
}
