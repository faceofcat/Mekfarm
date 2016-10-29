package mekfarm.inventories;

import mekfarm.MekfarmMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Created by CF on 2016-10-29.
 */
public class CombinedStackHandler implements IItemHandlerModifiable {
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
            else {
                MekfarmMod.logger.warn("CombinedStackHandler::setStackInSlot : Incoming item handler is not modifiable.");
            }
        }
        else {
            if (this.outcome instanceof IItemHandlerModifiable) {
                ((IItemHandlerModifiable)this.outcome).setStackInSlot(slot - this.getIncomeSlots(), stack);
            }
            else {
                MekfarmMod.logger.warn("CombinedStackHandler::setStackInSlot : Outgoing item handler is not modifiable.");
            }
        }
    }

    private int getIncomeSlots() {
        return (this.income != null) ? this.income.getSlots() : 0;
    }

    private int getOutcomeSlots() {
        return (this.outcome != null) ? this.outcome.getSlots() : 0;
    }

    @Override
    public int getSlots() {
        return this.getIncomeSlots() + this.getOutcomeSlots();
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
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < this.getIncomeSlots()) {
            return this.income.insertItem(slot, stack, simulate);
        }
        else {
            return this.outcome.insertItem(slot - this.getIncomeSlots(), stack, simulate);
        }
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
}
