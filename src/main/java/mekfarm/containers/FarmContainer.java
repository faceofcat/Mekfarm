package mekfarm.containers;

import mekfarm.common.IInteractiveEntity;
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
public class FarmContainer extends Container implements IInitializableContainer {

    private TileEntity te;

    public FarmContainer() {
    }

    @Override
    public void initialize(IInventory playerInventory, TileEntity te) {
        this.te = te;

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 10 + col * 18;
                int y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 10 + row * 18;
            int y = 58 + 70;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Add 'inputs'
        int x = 118;
        int y = 6;
        int slotIndex = 0;
        for (int i = 0; i < Math.min(3, itemHandler.getSlots()); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
        }
        x = 118;
        y = 42;
        for (int i = 3; i < itemHandler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
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

            if (itemstack1.stackSize == 0) {
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
}