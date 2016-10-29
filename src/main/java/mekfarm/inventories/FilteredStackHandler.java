package mekfarm.inventories;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by CF on 2016-10-29.
 */
public class FilteredStackHandler extends ItemStackHandler {
    FilteredStackHandler(int size) {
        super(size);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if ((stack != null) && (this.acceptsStack(slot, stack, true) == false)) {
            return;
        }

        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.insertItem(slot, stack, simulate, false);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean internal) {
        if ((stack == null) || (stack.stackSize == 0)) {
            return null;
        }

        if ((internal == false) && (this.acceptsStack(slot, stack, internal) == false)) {
            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }

    protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
        return true;
    }
}