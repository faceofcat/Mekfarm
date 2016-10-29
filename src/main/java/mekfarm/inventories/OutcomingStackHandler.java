package mekfarm.inventories;

import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2016-10-29.
 */
public class OutcomingStackHandler extends FilteredStackHandler {
    public OutcomingStackHandler(int size) {
        super(size);
    }
//
//    @Override
//    protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
//        return internal;
//    }

    public ItemStack insertItems(ItemStack stack, boolean simulate) {
        for (int i = 0; i < this.getSlots(); i++) {
            stack = super.insertItem(i, stack, simulate, true);
            if ((stack == null) || (stack.stackSize == 0)) {
                break;
            }
        }
        return stack;
    }
}
