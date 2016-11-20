package mekfarm.inventories;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by CF on 2016-10-29.
 */
public class FilteredStackHandler extends ItemStackHandler implements IInternalItemHandler {
    FilteredStackHandler(int size) {
        super(size);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.setStackInSlot(slot, stack, false);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack, boolean internal) {
        if ((stack != null) && (this.acceptsStack(slot, stack, internal) == false)) {
            return;
        }

        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack getStackInSlot(int slot, boolean internal) {
        return super.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.insertItem(slot, stack, simulate, false);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean internal) {
        if ((stack == null) || (stack.getCount() == 0)) {
            return null;
        }

        if ((internal == false) && (this.acceptsStack(slot, stack, internal) == false)) {
            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate){
        return this.extractItem(slot, amount, simulate, false);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean internal) {
        if (this.canExtract(slot, amount, internal))
            return super.extractItem(slot, amount, simulate);
        return null;
    }

    protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
        return true;
    }

    protected boolean canExtract(int slot, int amount, boolean internal) { return true; }
}