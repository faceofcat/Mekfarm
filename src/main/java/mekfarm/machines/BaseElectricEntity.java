package mekfarm.machines;

import mekanism.api.energy.IStrictEnergyAcceptor;
import mekfarm.MekfarmMod;
import mekfarm.capabilities.IMachineInfo;
import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.IContainerProvider;
import mekfarm.common.IInteractiveEntity;
import mekfarm.common.IWorkProgress;
import mekfarm.inventories.*;
import mekfarm.net.ISimpleNBTMessageHandler;
import mekfarm.net.SimpleNBTMessage;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by CF on 2016-11-04.
 */
public abstract class BaseElectricEntity<CT extends Container, CGT extends GuiContainer> extends TileEntity implements ITickable, ISimpleNBTMessageHandler, IContainerProvider, IInteractiveEntity, IWorkProgress, IMachineInfo {
    private static final int SYNC_ON_TICK = 20;
    private int syncTick = SYNC_ON_TICK;

    private int lastWorkTicks = 0;
    private int workTick = 0;

    protected EnergyStorage energyStorage;
    protected IncomingStackHandler inStackHandler;
    protected OutcomingStackHandler outStackHandler;
    protected CombinedStackHandler allStackHandler;

    protected FiltersStackHandler filtersHandler;

    private int typeId; // used for message sync

    private Class<CT> containerClass;
    private Class<CGT> guiContainerClass;

    protected BaseElectricEntity(int typeId, int energyMaxStorage, int inputSlots, int outputSlots, int filterSlots, Class<CT> containerClass, Class<CGT> guiContainerClass) {
        this.typeId = typeId;

        this.energyStorage = new EnergyStorage(energyMaxStorage) {
            @Override
            public void onChanged() {
                BaseElectricEntity.this.markDirty();
                BaseElectricEntity.this.forceSync();
            }
        };
        this.inStackHandler = new IncomingStackHandler(inputSlots) {
            @Override
            protected void onContentsChanged(int slot) {
                BaseElectricEntity.this.markDirty();
                BaseElectricEntity.this.forceSync();
            }

            @Override
            protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
                return BaseElectricEntity.this.acceptsInputStack(slot, stack, internal);
            }
        };
        this.outStackHandler = new OutcomingStackHandler(outputSlots) {
            @Override
            protected void onContentsChanged(int slot) {
                BaseElectricEntity.this.markDirty();
                BaseElectricEntity.this.forceSync();
            }
        };
        this.allStackHandler = new CombinedStackHandler(this.inStackHandler, this.outStackHandler);

        this.filtersHandler = (filterSlots <= 0) ? null : new FiltersStackHandler(filterSlots) {
            @Override
            public boolean acceptsFilter(int slot, ItemStack filter) {
                // TODO: maybe add more stuff to this
                return super.acceptsFilter(slot, filter);
            }
        };

        this.containerClass = containerClass;
        this.guiContainerClass = guiContainerClass;
    }

    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        return true;
    }

    private void forceSync() {
        if (!this.worldObj.isRemote) {
            this.syncTick = SYNC_ON_TICK;
        }
    }

    @Override
    public float getWorkProgress() {
        if (this.lastWorkTicks <= 0) {
            return 0;
        }
        return (float)Math.min(this.lastWorkTicks, Math.max(0, this.workTick)) / (float)this.lastWorkTicks;
    }

    protected int getWorkTicks() {
        return 40;
    }

    protected int getEnergyForWork() {
        return 500;
    }

    protected int getEntityTypeId() { return this.typeId; }

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
        if (compound.hasKey("filters") && (this.filtersHandler != null)) {
            this.filtersHandler.deserializeNBT(compound.getCompoundTag("filters"));
        }
        this.lastWorkTicks = compound.getInteger("tick_lastWork");
        this.workTick = compound.getInteger("tick_work");
        this.syncTick = compound.getInteger("tick_sync");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("income", this.inStackHandler.serializeNBT());
        compound.setTag("outcome", this.outStackHandler.serializeNBT());
        compound.setTag("energy", this.energyStorage.serializeNBT());
        if (this.filtersHandler != null) {
            compound.setTag("filters", this.filtersHandler.serializeNBT());
        }
        compound.setInteger("tick_work", this.workTick);
        compound.setInteger("tick_lastWork", this.lastWorkTicks);
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
        if (this.worldObj.isRemote) {
            NBTTagCompound compound = (message == null) ? null : message.getCompound();
            if (compound != null) {
                int tetId = compound.getInteger("__tetId");
                if (tetId == this.getEntityTypeId()) {
                    this.processServerMessage(compound);
                }
            }
        }
        return null;
    }

    protected void processServerMessage(NBTTagCompound compound) {
        this.readFromNBT(compound);
    }

    protected abstract float performWork();

    @Override
    public void update() {
        this.workTick++;
        if (this.workTick > this.lastWorkTicks) {
            this.lastWorkTicks = this.getWorkTicks();
            this.workTick = 0;

            if (!this.worldObj.isRemote) {
                int energy = this.getEnergyForWork();
                if (this.energyStorage.getEnergyStored() >= energy) {
                    float work = this.performWork();
                    if (work > 0) {
                        this.energyStorage.extractEnergy(Math.round(energy * work), false, true);
                        this.forceSync();
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
        EnumFacing machineFacing = BlocksRegistry.animalFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(BlocksRegistry.animalFarmBlock.FACING);
        Boolean isFront = (machineFacing == facing);

//        MekfarmMod.logger.info("Tested for capability: " + capability.getName());

        if ((capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && !isFront) {
            return true;
        }
        else if (!isFront && (capability == CapabilityEnergy.ENERGY)) {
            return true;
        }
        else if ((this.filtersHandler != null) && (capability == MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER)) {
            return true;
        }
        else if (capability == MekfarmCapabilities.CAPABILITY_MACHINE_INFO) {
            return true;
        }

        if (Loader.isModLoaded("tesla") && this.hasTeslaCapability(capability, facing, isFront)) {
            return true;
        }
        if (Loader.isModLoaded("Mekanism") && !isFront && (capability.getName() == "mekanism.api.energy.IStrictEnergyAcceptor")) {
            return true; // TODO: not sure if this is the best way :S
        }

        return super.hasCapability(capability, facing);
    }

    @Optional.Method(modid = "tesla")
    private boolean hasTeslaCapability(Capability<?> capability, EnumFacing facing, boolean isFront) {
        if (!isFront && ((capability == TeslaCapabilities.CAPABILITY_HOLDER) || (capability == TeslaCapabilities.CAPABILITY_CONSUMER))) {
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T>T getCapability(Capability<T> capability, EnumFacing facing) {
        EnumFacing machineFacing = BlocksRegistry.animalFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(BlocksRegistry.animalFarmBlock.FACING);
        Boolean isFront = (machineFacing == facing);

//        MekfarmMod.logger.info("Asked for capability: " + capability.getName());

        if (!isFront && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return (T)this.allStackHandler;
        }
        else if (!isFront && (capability == CapabilityEnergy.ENERGY)) {
            return (T)this.energyStorage;
        }
        else if ((this.filtersHandler != null) && (capability == MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER)) {
            return (T)this.filtersHandler;
        }
        else if (capability == MekfarmCapabilities.CAPABILITY_MACHINE_INFO) {
            return (T)this;
        }

        if (Loader.isModLoaded("tesla")) {
            Object teslaThing = this.getTeslaCapability(capability, facing, isFront);
            if (teslaThing != null) {
                return (T) teslaThing;
            }
        }
        if (Loader.isModLoaded("Mekanism") && !isFront && (capability.getName() == "mekanism.api.energy.IStrictEnergyAcceptor")) {
            return (T)this.energyStorage;
        }

        return super.getCapability(capability, facing);
    }

    @Optional.Method(modid = "tesla")
    private <T>T getTeslaCapability(Capability<T> capability, EnumFacing facing, boolean isFront) {
        if (!isFront && ((capability == TeslaCapabilities.CAPABILITY_HOLDER) || (capability == TeslaCapabilities.CAPABILITY_CONSUMER))) {
            return (T)this.energyStorage;
        }
        return null;
    }

    @Override
    public Container getContainer(IInventory playerInventory) {
        CT container = null;
        try {
            Constructor<CT> c = this.containerClass.getConstructor(IInventory.class, TileEntity.class);
            if (c != null) {
                container = c.newInstance(playerInventory, this);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            MekfarmMod.logger.error("Error getting container", e);
        }
        return container;
    }

    @Override
    public GuiContainer getContainerGUI(IInventory playerInventory) {
        CGT gui = null;
        try {
            Constructor<CGT> c = this.guiContainerClass.getConstructor(TileEntity.class, Container.class);
            if (c != null) {
                Container container = this.getContainer(playerInventory);
                if (container != null) {
                    gui = c.newInstance(this, container);
                }
            }
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            MekfarmMod.logger.error("Error getting container gui", e);
        }
        return gui;
    }

    @Override
    public String getUnlocalizedMachineName(){
        return this.getBlockType().getUnlocalizedName() + ".name";
    }
}
