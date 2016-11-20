package mekfarm.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
    protected Slot createInputSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition) {
        return new TexturedSlot(itemHandler, slotIndex, xPosition, yPosition, 117, 226, 5);
    }
}
