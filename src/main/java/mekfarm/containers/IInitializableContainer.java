package mekfarm.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-11-06.
 */
public interface IInitializableContainer {
    void initialize(IInventory playerInventory, TileEntity te);
}
