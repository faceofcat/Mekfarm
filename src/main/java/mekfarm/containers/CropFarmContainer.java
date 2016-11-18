package mekfarm.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by CF on 2016-11-16.
 */
public class CropFarmContainer extends FarmContainer {
    public CropFarmContainer(IInventory playerInventory, TileEntity te) {
        super(playerInventory, te);
    }

    @Override
    protected void addInputSlots(IItemHandler itemHandler, int inputs) {
        super.addInputSlots(itemHandler, inputs);
    }
}
