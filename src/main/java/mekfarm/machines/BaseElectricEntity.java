package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.capabilities.IMachineInfo;
import mekfarm.capabilities.MekfarmCapabilities;
import mekfarm.common.IContainerProvider;
import mekfarm.common.IInteractiveEntity;
import mekfarm.inventories.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.ndrei.teslacorelib.tileentity.ElectricTileEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by CF on 2016-11-04.
 */
public abstract class BaseElectricEntity<CT extends Container, CGT extends GuiContainer> extends ElectricTileEntity implements IContainerProvider, IInteractiveEntity, IMachineInfo {
    protected IncomingStackHandler inStackHandler;
    protected OutcomingStackHandler outStackHandler;
    protected CombinedStackHandler allStackHandler;

    protected FiltersStackHandler filtersHandler;

    private Class<CT> containerClass;
    private Class<CGT> guiContainerClass;

    protected BaseElectricEntity(int typeId, int energyMaxStorage, int inputSlots, int outputSlots, int filterSlots, Class<CT> containerClass, Class<CGT> guiContainerClass) {
        super(typeId);

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

        this.filtersHandler = (filterSlots <= 0) ? null : new FiltersStackHandler(this, filterSlots) {
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

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("income")) {
            this.inStackHandler.deserializeNBT(compound.getCompoundTag("income"));
        }
        if (compound.hasKey("outcome")) {
            this.outStackHandler.deserializeNBT(compound.getCompoundTag("outcome"));
        }
        if (compound.hasKey("filters") && (this.filtersHandler != null)) {
            this.filtersHandler.deserializeNBT(compound.getCompoundTag("filters"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setTag("income", this.inStackHandler.serializeNBT());
        compound.setTag("outcome", this.outStackHandler.serializeNBT());
        if (this.filtersHandler != null) {
            compound.setTag("filters", this.filtersHandler.serializeNBT());
        }

        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    protected boolean testBlockState() {
        return (this.getWorld().getBlockState(this.getPos()).getBlock() instanceof BaseOrientedBlock);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        EnumFacing machineFacing = this.testBlockState()
                ? this.getWorld().getBlockState(this.getPos()).getValue(BaseOrientedBlock.FACING)
                : null;
        Boolean isFront = (machineFacing != null) && (machineFacing == facing);

        if ((capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && !isFront) {
            return true;
        } else if ((this.filtersHandler != null) && (capability == MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER)) {
            return true;
        } else if (capability == MekfarmCapabilities.CAPABILITY_MACHINE_INFO) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        EnumFacing machineFacing = this.testBlockState()
                ? this.getWorld().getBlockState(this.getPos()).getValue(BaseOrientedBlock.FACING)
                : null;
        Boolean isFront = (machineFacing != null) && (machineFacing == facing);

        if (!isFront && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return (T) this.allStackHandler;
        } else if ((this.filtersHandler != null) && (capability == MekfarmCapabilities.CAPABILITY_FILTERS_HANDLER)) {
            return (T) this.filtersHandler;
        } else if (capability == MekfarmCapabilities.CAPABILITY_MACHINE_INFO) {
            return (T) this;
        }

        return super.getCapability(capability, facing);
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
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            MekfarmMod.logger.error("Error getting container gui", e);
        }
        return gui;
    }

    @Override
    public String getUnlocalizedMachineName() {
        return this.getBlockType().getUnlocalizedName() + ".name";
    }
}
