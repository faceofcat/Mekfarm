package mekfarm.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by CF on 2016-11-12.
 */
public class AnimalFarmContainer extends FarmContainer {
    public AnimalFarmContainer(IInventory playerInventory, TileEntity entity) {
        super(playerInventory, entity);
    }

    @Override
    protected Slot createInputSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition) {
        return new TexturedSlot(itemHandler, slotIndex, xPosition, yPosition, 117, 172, 6);
    }
}
