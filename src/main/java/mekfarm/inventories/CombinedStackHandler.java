package mekfarm.inventories;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Created by CF on 2016-10-29.
 */
public class CombinedStackHandler implements IItemHandlerModifiable, IInternalItemHandler, IInputOutputItemHandler {
    private IItemHandler income, outcome;

    public CombinedStackHandler(IItemHandler income, IItemHandler outcome) {
        this.income = income;
        this.outcome = outcome;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot < this.getIncomeSlots()) {
            if (this.income instanceof IItemHandlerModifiable) {
                ((IItemHandlerModifiable)this.income).setStackInSlot(slot, stack);
            }
        }
        else {
            if (this.outcome instanceof IItemHandlerModifiable) {
                ((IItemHandlerModifiable)this.outcome).setStackInSlot(slot - this.getIncomeSlots(), stack);
            }
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack, boolean internal) {
        if (slot < this.getIncomeSlots()) {
            if (this.income instanceof IInternalItemHandler) {
                ((IInternalItemHandler)this.income).setStackInSlot(slot, stack, internal);
                return;
            }
        }
        else {
            if (this.outcome instanceof IInternalItemHandler) {
                ((IInternalItemHandler)this.outcome).setStackInSlot(slot - this.getIncomeSlots(), stack, internal);
                return;
            }
        }
        this.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.getIncomeSlots() + this.getOutcomeSlots();
    }

    private int getIncomeSlots() {
        return (this.income != null) ? this.income.getSlots() : 0;
    }

    private int getOutcomeSlots() {
        return (this.outcome != null) ? this.outcome.getSlots() : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < this.getIncomeSlots()) {
            return this.income.getStackInSlot(slot);
        }
        else {
            return this.outcome.getStackInSlot(slot - this.getIncomeSlots());
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot, boolean internal) {
        if (slot < this.getIncomeSlots()) {
            if (this.income instanceof IInternalItemHandler) {
                return ((IInternalItemHandler) this.income).getStackInSlot(slot, internal);
            }
        } else {
            if (this.outcome instanceof IInternalItemHandler) {
                return ((IInternalItemHandler) this.outcome).getStackInSlot(slot - this.getIncomeSlots(), internal);
            }
        }
        return this.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < this.getIncomeSlots()) {
            return this.income.insertItem(slot, stack, simulate);
        }
        else {
            return this.outcome.insertItem(slot - this.getIncomeSlots(), stack, simulate);
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate, boolean internal) {
        if (slot < this.getIncomeSlots()) {
            if (this.income instanceof IInternalItemHandler) {
                return ((IInternalItemHandler) this.income).insertItem(slot, stack, simulate, internal);
            }
        } else {
            if (this.outcome instanceof IInternalItemHandler) {
                return ((IInternalItemHandler) this.outcome).insertItem(slot - this.getIncomeSlots(), stack, simulate, internal);
            }
        }
        return this.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < this.getIncomeSlots()) {
            return this.income.extractItem(slot, amount, simulate);
        }
        else {
            return this.outcome.extractItem(slot - this.getIncomeSlots(), amount, simulate);
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean internal) {
        if (slot < this.getIncomeSlots()) {
            if (this.income instanceof IInternalItemHandler) {
                return ((IInternalItemHandler) this.income).extractItem(slot, amount, simulate, internal);
            }
        } else {
            if (this.outcome instanceof IInternalItemHandler) {
                return ((IInternalItemHandler) this.outcome).extractItem(slot - this.getIncomeSlots(), amount, simulate, internal);
            }
        }
        return this.extractItem(slot, amount, simulate);
    }

    @Override
    public int getInputSlots() {
        return (this.income == null) ? 0 : this.income.getSlots();
    }

    @Override
    public int getOutputSlots() {
        return (this.outcome == null) ? 0 : this.outcome.getSlots();
    }
}
