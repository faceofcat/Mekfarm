package mekfarm.containers;

import mekfarm.inventories.IInternalItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by CF on 2016-11-07.
 */
public class InternalSlot extends SlotItemHandler {
    private ItemHandlerProxy proxy = null;

    public InternalSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public IItemHandler getItemHandler()
    {
        IItemHandler handler = super.getItemHandler();
        if (handler instanceof IInternalItemHandler) {
            return (this.proxy == null) ? (this.proxy = new ItemHandlerProxy((IInternalItemHandler)handler)) : this.proxy;
        }
        return handler;
    }

    private class ItemHandlerProxy implements IItemHandlerModifiable {
        private IInternalItemHandler handler;

        public ItemHandlerProxy(IInternalItemHandler handler) {
            this.handler = handler;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            this.handler.setStackInSlot(slot, stack, false);
        }

        @Override
        public int getSlots() {
            return this.handler.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return this.handler.getStackInSlot(slot, true);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return this.handler.insertItem(slot, stack, simulate, false);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return this.handler.extractItem(slot, amount, simulate, true);
        }
    }
}
