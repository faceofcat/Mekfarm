package mekfarm.inventories;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/**
 * Created by CF on 2016-10-29.
 */
public class FilteredStackHandler extends ItemStackHandler implements IInternalItemHandler {
    FilteredStackHandler(int size) {
        super(size);
    }

//    @Override
//    public final void setStackInSlot(int slot, ItemStack stack) {
//        this.setStackInSlot(slot, stack, false);
//    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack, boolean internal) {
//        if ((stack != null) && (this.acceptsStack(slot, stack, internal) == false)) {
//            return;
//        }

        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack getStackInSlot(int slot, boolean internal) {
        return super.getStackInSlot(slot);
    }

    @Override
    public final ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.insertItem(slot, stack, simulate, false);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean internal) {
        if ((stack == null) || stack.isEmpty() || (stack.getCount() == 0)) {
            return ItemStack.EMPTY;
        }

        if ((internal == false) && (this.acceptsStack(slot, stack, internal) == false)) {
            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public final ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.extractItem(slot, amount, simulate, false);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean internal) {
        if (this.canExtract(slot, amount, internal))
            return super.extractItem(slot, amount, simulate);
        return ItemStack.EMPTY;
    }

    protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
        return true;
    }

    protected boolean canExtract(int slot, int amount, boolean internal) {
        return true;
    }

    public final ItemStack distributeItems(ItemStack stack, boolean simulate) {
        return this.distributeItems(stack, simulate, false);
    }

    public ItemStack distributeItems(ItemStack stack, boolean simulate, boolean ignoreEmptySlots) {
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack existing = this.getStackInSlot(i, true);
            if (!ignoreEmptySlots || ((existing != null) && !existing.isEmpty())) {
                stack = this.insertItem(i, stack, simulate, true);
                if ((stack == null) || stack.isEmpty() || (stack.getCount() == 0)) {
                    break;
                }
            }
        }
        return stack;
    }

    public List<ItemStack> getCombinedInventory() {
        List<ItemStack> list = Lists.newArrayList();
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i, true);
            if ((stack == null) || stack.isEmpty()) {
                continue;
            }

            ItemStack match = null;
            for (ItemStack existing : list) {
                if (existing.getItem() == stack.getItem()) {
                    match = existing;
                    break;
                }
            }
            if (match == null) {
                list.add(stack.copy());
            } else {
                match.setCount(match.getCount() + stack.getCount());
            }
        }
        return list;
    }

    public int extractFromCombinedInventory(ItemStack stack, int amount) {
        if ((stack == null) || stack.isEmpty() || (stack.getCount() == 0)) {
            return 0;
        }

        int taken = 0;
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack temp = this.getStackInSlot(i, true);
            if ((temp == null) || temp.isEmpty() || (temp.getItem() != stack.getItem())) {
                continue;
            }

            ItemStack takenStack = this.extractItem(i, Math.min(amount, temp.getCount()), false, true);
            taken += takenStack.getCount();
            amount -= takenStack.getCount();
            if (amount <= 0) {
                break;
            }
        }
        return taken;
    }
}