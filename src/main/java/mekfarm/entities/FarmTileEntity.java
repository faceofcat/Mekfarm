package mekfarm.entities;

import com.sun.jna.platform.win32.WinUser;
import mekfarm.MekfarmMod;
import mekfarm.blocks.FarmBlock;
import mekfarm.inventories.CombinedStackHandler;
import mekfarm.inventories.EnergyStorage;
import mekfarm.inventories.IncomingStackHandler;
import mekfarm.inventories.OutcomingStackHandler;
import mekfarm.items.AnimalPackage;
import mekfarm.net.ISimpleNBTMessageHandler;
import mekfarm.net.SimpleNBTMessage;
import net.darkhax.tesla.Tesla;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.IFMLSidedHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmTileEntity extends TileEntity implements ITickable, ISimpleNBTMessageHandler {
    public static final int INPUT_SIZE = 3;
    public static final int OUTPUT_SIZE = 3;
    public static final int INVENTORY_SIZE = INPUT_SIZE + OUTPUT_SIZE;

    private static final int SYNC_ON_TICK = 20;
    private int syncTick = SYNC_ON_TICK;

    private static final int WORK_ON_TICK = 20;
    private int workTick = 0;

    private EnergyStorage energyStorage = new EnergyStorage(500000) {
        @Override
        public void onChanged() {
            FarmTileEntity.this.markDirty();
            FarmTileEntity.this.forceSync();
        }
    };

    private IncomingStackHandler inStackHandler = new IncomingStackHandler(INPUT_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            FarmTileEntity.this.markDirty();
            FarmTileEntity.this.forceSync();
        }
    };

    private OutcomingStackHandler outStackHandler = new OutcomingStackHandler(OUTPUT_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            FarmTileEntity.this.markDirty();
            FarmTileEntity.this.forceSync();
        }
    };

    private CombinedStackHandler allStackHandler = new CombinedStackHandler(this.inStackHandler, this.outStackHandler);

    private void forceSync() {
        if (!this.worldObj.isRemote) {
            this.syncTick = SYNC_ON_TICK;
        }
    }

    public float getWorkProgress() {
        return (float)Math.min(WORK_ON_TICK, Math.max(0, this.workTick)) / (float)WORK_ON_TICK;
    }

    private int getEntityTypeId() {
        return 1;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("income")) {
            this.inStackHandler.deserializeNBT(compound.getCompoundTag("income"));
        }
        if (compound.hasKey("outcome")) {
            this.outStackHandler.deserializeNBT(compound.getCompoundTag("outcome"));
        }
        if (compound.hasKey("energy")) {
            this.energyStorage.deserializeNBT(compound.getCompoundTag("energy"));
        }
        this.workTick = compound.getInteger("tick_work");
        this.syncTick = compound.getInteger("tick_sync");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("income", this.inStackHandler.serializeNBT());
        compound.setTag("outcome", this.outStackHandler.serializeNBT());
        compound.setTag("energy", this.energyStorage.serializeNBT());
        compound.setInteger("tick_work", this.workTick);
        compound.setInteger("tick_sync", this.syncTick);
        return compound;
    }

    private NBTTagCompound writeToNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("__tetId", this.getEntityTypeId());
        return this.writeToNBT(compound);
    }

    @Override
    public SimpleNBTMessage handleMessage(SimpleNBTMessage message) {
//        MekfarmMod.logger.info("Message received: " + message.getCompound().toString());
//        if (MekfarmMod.getSide() == Side.CLIENT) {
        if (this.worldObj.isRemote) {
            NBTTagCompound compound = (message == null) ? null : message.getCompound();
            if (compound != null) {
                int tetId = compound.getInteger("__tetId");
                if (tetId == this.getEntityTypeId()) {
                    this.readFromNBT(compound);
                }
            }
        }
        return null;
    }

    @Override
    public void update() {
//            List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(
//                    getPos().add(-10, -10, -10), getPos().add(10, 10, 10)));
//            MekfarmMod.logger.info("There are " + list.size() + " entities around the farm.");
//            for (EntityAnimal thing : list) {
//                MekfarmMod.logger.info("  - " + thing.toString() + " : " + thing.getClass().getSimpleName() + " : " + thing.getAge() + " : " + thing.getGrowingAge());
//            }

        this.workTick++;
        if (this.workTick > WORK_ON_TICK) {
            this.workTick = 0;

            if (!this.worldObj.isRemote) {
                ItemStack stack = null;
                int stackIndex = 0;
                for (; stackIndex < this.inStackHandler.getSlots(); stackIndex++) {
                    stack = this.inStackHandler.extractItem(stackIndex, 1, true);
                    if ((stack != null) && (stack.stackSize > 0)) {
                        break;
                    }
                }
                if ((stack != null) && (stack.stackSize > 0)) {
                    int original = stack.stackSize;
                    ItemStack stackCopy = stack.copy();
                    if (stackCopy.getItem() instanceof AnimalPackage) {
                        stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
                    }
                    ItemStack finalStack = this.outStackHandler.insertItems(stackCopy, false);
                    int inserted = original - ((finalStack == null) ? 0 : finalStack.stackSize);
                    // if ((finalStack != null) && (finalStack.stackSize > 0)) {
                    if (inserted > 0) {
                        // this.inStackHandler.insertItem(stackIndex, stack, false, true);
                        this.inStackHandler.extractItem(stackIndex, inserted, false);
                    }
                }
            }
        }

        if (!this.worldObj.isRemote) {
            this.syncTick++;
            if (this.syncTick >= SYNC_ON_TICK) {
                MekfarmMod.network.send(new SimpleNBTMessage(this, this.writeToNBT()));
                this.syncTick = 0;
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
        else if ((capability == TeslaCapabilities.CAPABILITY_HOLDER) || (capability == TeslaCapabilities.CAPABILITY_CONSUMER) || (capability == CapabilityEnergy.ENERGY)) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T>T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.WEST) {
                return (T)this.inStackHandler;
            }
            else if ((facing == EnumFacing.EAST) || (facing == EnumFacing.DOWN)) {
                return (T)this.outStackHandler;
            }
            else if ((facing == null) || (facing == EnumFacing.UP)) {
                return (T)this.allStackHandler;
            }
        }
        else if ((capability == TeslaCapabilities.CAPABILITY_HOLDER) || (capability == TeslaCapabilities.CAPABILITY_CONSUMER) || (capability == CapabilityEnergy.ENERGY)) {
            // MekfarmMod.logger.info("getCapability: energy. " + capability.toString());
            return (T)this.energyStorage;
        }
        return super.getCapability(capability, facing);
    }
}
