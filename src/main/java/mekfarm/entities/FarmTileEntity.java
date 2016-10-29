package mekfarm.entities;

import com.sun.jna.platform.win32.WinUser;
import mekfarm.MekfarmMod;
import mekfarm.inventories.CombinedStackHandler;
import mekfarm.inventories.IncomingStackHandler;
import mekfarm.inventories.OutcomingStackHandler;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmTileEntity extends TileEntity implements ITickable {
    public static final int INPUT_SIZE = 3;
    public static final int OUTPUT_SIZE = 9;
    public static final int INVENTORY_SIZE = INPUT_SIZE + OUTPUT_SIZE;
    private int delayCounter = 20;

//    private ItemStackHandler itemStackHandler = new ItemStackHandler(INVENTORY_SIZE) {
//        @Override
//        protected void onContentsChanged(int slot) {
//            FarmTileEntity.this.markDirty();
//        }
//
//        @Override
//        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
//            MekfarmMod.logger.info("inventory insert: " + slot + " : " + stack.toString() + " : " + simulate);
//            if (slot >= 3) {
//                // cannot insert in 'output' inventory slots
//                return stack;
//            }
//
//            return super.insertItem(slot, stack, simulate);
//        }
//    };

    private IncomingStackHandler inStackHandler = new IncomingStackHandler(INPUT_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            FarmTileEntity.this.markDirty();
        }
    };

    private OutcomingStackHandler outStackHandler = new OutcomingStackHandler(OUTPUT_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            FarmTileEntity.this.markDirty();
        }
    };

    private CombinedStackHandler allStackHandler = new CombinedStackHandler(this.inStackHandler, this.outStackHandler);

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("income")) {
            // itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
            this.inStackHandler.deserializeNBT(compound.getCompoundTag("income"));
        }
        if (compound.hasKey("outcome")) {
            this.outStackHandler.deserializeNBT(compound.getCompoundTag("outcome"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        // compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setTag("income", this.inStackHandler.serializeNBT());
        compound.setTag("outcome", this.outStackHandler.serializeNBT());
        return compound;
    }

    @Override
    public void update() {
        // This method is called every tick (20 times per second normally)
        this.delayCounter --;
        if (this.delayCounter >= 0) {
            return;
        }
        this.delayCounter = 10; // 100; // should be 5 seconds?

        if (!worldObj.isRemote) {
//            List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(
//                    getPos().add(-10, -10, -10), getPos().add(10, 10, 10)));
//            MekfarmMod.logger.info("There are " + list.size() + " entities around the farm.");
//            for (EntityAnimal thing : list) {
//                MekfarmMod.logger.info("  - " + thing.toString() + " : " + thing.getClass().getSimpleName() + " : " + thing.getAge() + " : " + thing.getGrowingAge());
//            }

            ItemStack stack = null;
            int stackIndex = 0;
            for(; stackIndex < this.inStackHandler.getSlots(); stackIndex++) {
                stack = this.inStackHandler.extractItem(stackIndex, 1, false);
                if ((stack != null) && (stack.stackSize > 0)) {
                    break;
                }
            }
            if ((stack != null) && (stack.stackSize > 0)) {
                ItemStack finalStack = this.outStackHandler.insertItems(stack, false);
                if ((finalStack != null) && (finalStack.stackSize > 0)) {
                    this.inStackHandler.insertItem(stackIndex, stack, false, true);
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T>T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            // return (T)itemStackHandler;
            if (facing == EnumFacing.WEST) {
                return (T)this.inStackHandler;
            }
            else if (facing == EnumFacing.EAST) {
                return (T)this.outStackHandler;
            }
            else if ((facing == null) || (facing == EnumFacing.UP)) {
                return (T)this.allStackHandler;
            }
        }
        return super.getCapability(capability, facing);
    }
}
