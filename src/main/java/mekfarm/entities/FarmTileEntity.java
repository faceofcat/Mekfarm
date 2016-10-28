package mekfarm.entities;

import mekfarm.MekfarmMod;
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
    public static final int INVENTORY_SIZE = 12;

    private int delayCounter = 20;

    private ItemStackHandler itemStackHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            FarmTileEntity.this.markDirty();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot >3) {
                // cannot insert in 'output' inventory slots
                return null;
            }

            return super.insertItem(slot, stack, simulate);
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        return compound;
    }

    @Override
    public void update() {
        // This method is called every tick (20 times per second normally)
        this.delayCounter --;
        if (this.delayCounter >= 0) {
            return;
        }
        this.delayCounter = 100; // should be 5 seconds?

        if (worldObj.isRemote) {
            List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(
                    getPos().add(-10, -10, -10), getPos().add(10, 10, 10)));
            MekfarmMod.logger.info("There are " + list.size() + " entities around the farm.");
            for (EntityAnimal thing : list) {
                MekfarmMod.logger.info("  - " + thing.toString() + " : " + thing.getClass().getSimpleName() + " : " + thing.getAge() + " : " + thing.getGrowingAge());
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
            return (T)itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }
}
