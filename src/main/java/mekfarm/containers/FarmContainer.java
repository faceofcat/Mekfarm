package mekfarm.containers;

import mekfarm.entities.FarmTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainer extends Container {

    private FarmTileEntity te;

    public FarmContainer(IInventory playerInventory, FarmTileEntity te) {
        this.te = te;

        // This container references items out of our own inventory (the 9 slots we hold ourselves)
        // as well as the slots from the player inventory so that the user can transfer items between
        // both inventories. The two calls below make sure that slots are defined for both inventories.
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18;
                int y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18;
            int y = 58 + 70;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Add 'inputs'
        int x = 63;
        int y = 6;
        int slotIndex = 0;
        for (int i = 0; i < Math.min(3, itemHandler.getSlots()); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
        }
        x = 9;
        y = 42;
        for (int i = 3; i < itemHandler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
        }
    }

//    @Nullable
//    @Override
//    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
//        ItemStack itemstack = null;
//        Slot slot = this.inventorySlots.get(index);
//
//        if ((slot != null) && slot.getHasStack()) {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            if (index < FarmTileEntity.INVENTORY_SIZE) {
//                if (!this.mergeItemStack(itemstack1, FarmTileEntity.INVENTORY_SIZE, this.inventorySlots.size(), true)) {
//                    return null;
//                }
//            } else if (!this.mergeItemStack(itemstack1, 0, FarmTileEntity.INVENTORY_SIZE, false)) {
//                return null;
//            }
//
//            if (itemstack1.stackSize == 0) {
//                slot.putStack(null);
//            } else {
//                slot.onSlotChanged();
//            }
//        }
//
//        return itemstack;
//    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}