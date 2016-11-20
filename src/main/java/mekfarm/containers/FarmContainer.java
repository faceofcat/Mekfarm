package mekfarm.containers;

import mekfarm.capabilities.IFilterHandler;
import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.IInteractiveEntity;
import mekfarm.inventories.IInputOutputItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainer extends Container {

    private TileEntity te;

    public FarmContainer(IInventory playerInventory, TileEntity te) {
        this.te = te;

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 10 + col * 18;
                int y = row * 18 + 88;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 10 + row * 18;
            int y = 58 + 88;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int inputs;
        // int outputs;
        if (itemHandler instanceof IInputOutputItemHandler) {
            inputs = ((IInputOutputItemHandler)itemHandler).getInputSlots();
            // outputs = ((IInputOutputItemHandler)itemHandler).getOutputSlots();
        }
        else {
            inputs = Math.min(3, itemHandler.getSlots());
            // outputs = Math.max(0, itemHandler.getSlots() - inputs);
        }

        this.addInputSlots(itemHandler, inputs);
        this.addOutputSlots(itemHandler, inputs);

        IFilterHandler filterHandler = this.te.getCapability(MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER, null);
        if ((filterHandler != null) && (filterHandler.getSlots() > 0)) {
            for(int i = 0; i < Math.min(3, filterHandler.getSlots()); i++) {
                addSlotToContainer(new SlotItemHandler(filterHandler, i, 188, 6 + (i * 18)));
            }
        }
    }

    protected void addInputSlots(IItemHandler itemHandler, int inputs) {
        int x, y;
        for (int i = 0; i < inputs; i++) {
            x = 118 + (i * 18);
            y = 6;
            addSlotToContainer(this.createInputSlot(itemHandler, i, x, y));
        }
    }

    protected Slot createInputSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition) {
        return new InternalSlot(itemHandler, slotIndex, xPosition, yPosition);
    }

    protected void addOutputSlots(IItemHandler itemHandler, int inputs) {
        int x, y;
        for (int i = inputs; i < itemHandler.getSlots(); i++) {
            x = 118 + ((i - inputs) % 3) * 18;
            y = 42 + Math.floorDiv(i - inputs, 3) * 18;
            addSlotToContainer(new InternalSlot(itemHandler, i, x, y));
        }
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if ((slot != null) && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < itemHandler.getSlots()) {
                if (!this.mergeItemStack(itemstack1, itemHandler.getSlots(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, itemHandler.getSlots(), false)) {
                return null;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (this.te instanceof IInteractiveEntity) {
            return ((IInteractiveEntity)this.te).canInteractWith(playerIn);
        }
        return false;
    }
//
//    @Override
//    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
//        return super.slotClick(slotId, dragType, clickTypeIn, player);
//    }
}