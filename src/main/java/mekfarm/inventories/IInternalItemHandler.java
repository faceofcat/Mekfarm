package mekfarm.inventories;

import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2016-11-07.
 */
public interface IInternalItemHandler {
    int getSlots();

    void setStackInSlot(int slot, ItemStack stack, boolean internal);
    ItemStack getStackInSlot(int slot, boolean internal);
    ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean internal);
    ItemStack extractItem(int slot, int amount, boolean simulate, boolean internal);
}
