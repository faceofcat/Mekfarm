package mekfarm.machines;

import mekfarm.client.ClientProxy;
import mekfarm.common.FluidsRegistry;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.containers.BasicTeslaContainer;
import net.ndrei.teslacorelib.containers.FilteredSlot;
import net.ndrei.teslacorelib.gui.BasicRenderedGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.inventory.FilteredFluidTank;
import net.ndrei.teslacorelib.inventory.FluidTank;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by CF on 2017-03-09.
 */
public class LiquidXPStorageEntity extends SidedTileEntity {
    private ItemStackHandler inputItems;
    private ItemStackHandler outputItems;
    private FilteredFluidTank xpTank;

    public LiquidXPStorageEntity() {
        super(LiquidXPStorageEntity.class.getName().hashCode());
    }

    //#region inventories

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.inputItems = new ItemStackHandler(2) {
            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return 1;
                }
                return super.getStackLimit(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                LiquidXPStorageEntity.this.markDirty();
            }
        };
        this.outputItems = new ItemStackHandler(2) {
            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return 1;
                }
                return super.getStackLimit(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                LiquidXPStorageEntity.this.markDirty();
            }
        };
        this.xpTank = new FilteredFluidTank(FluidsRegistry.liquidXP, new FluidTank(1500) {
            @Override
            protected void onContentsChanged() {
                LiquidXPStorageEntity.this.markDirty();
            }
        });

        super.addInventory(new ColoredItemHandler(this.inputItems, EnumDyeColor.GREEN,
                "Input Liquid Containers", new BoundingRectangle(56, 25, 18, 54)) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return (slot == 0) && LiquidXPStorageEntity.this.isValidInContainer(stack);
            }

            @Override
            public boolean canExtractItem(int slot) {
                return (slot == 1);
            }

            @Override
            public List<Slot> getSlots(BasicTeslaContainer container) {
                List<Slot> slots =  super.getSlots(container);
                BoundingRectangle box = this.getBoundingBox();
                if (box != null) {
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), 0, box.getLeft() + 1, box.getTop() + 1));
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), 1, box.getLeft() + 1, box.getTop() + 1 + 36));
                }
                return slots;
            }
        });
        super.addInventoryToStorage(this.inputItems, "income");

        super.addFluidTank(this.xpTank, EnumDyeColor.LIME, "Liquid XP",
                new BoundingRectangle(79, 25, 18, 54));

        super.addInventory(new ColoredItemHandler(this.outputItems, EnumDyeColor.PURPLE,
                "Output Liquid Containers", new BoundingRectangle(102, 25, 18, 54)) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return (slot == 0) && LiquidXPStorageEntity.this.isValidOutContainer(stack);
            }

            @Override
            public boolean canExtractItem(int slot) {
                return (slot == 1);
            }

            @Override
            public List<Slot> getSlots(BasicTeslaContainer container) {
                List<Slot> slots =  super.getSlots(container);
                BoundingRectangle box = this.getBoundingBox();
                if (box != null) {
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), 0, box.getLeft() + 1, box.getTop() + 1));
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), 1, box.getLeft() + 1, box.getTop() + 1 + 36));
                }
                return slots;
            }
        });
        super.addInventoryToStorage(this.outputItems, "outcome");
    }

    private boolean isValidInContainer(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack)) {
            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handler != null) {
                    IFluidTankProperties[] tanks = handler.getTankProperties();
                    if ((tanks != null) && (tanks.length > 0)) {
                        for(IFluidTankProperties tank: tanks) {
                            if (tank.canDrain()) {
                                FluidStack content = tank.getContents();
                                if ((content != null) && (content.amount > 0) && (content.getFluid() == FluidsRegistry.liquidXP)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidOutContainer(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack)) {
            Item item = stack.getItem();
            if ((item == Items.GLASS_BOTTLE) || (item == Items.BUCKET)) {
                return true;
            }

            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handler != null) {
                    IFluidTankProperties[] tanks = handler.getTankProperties();
                    if ((tanks != null) && (tanks.length > 0)) {
                        for(IFluidTankProperties tank: tanks) {
                            if (tank.canFill()) {
                                FluidStack content = tank.getContents();
                                if ((content == null) || ((content.amount < tank.getCapacity()) && (content.getFluid() == FluidsRegistry.liquidXP))) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean shouldAddFluidItemsInventory() {
        return false;
    }

    //#endregion
    //#region gui

    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
        List<IGuiContainerPiece> list = super.getGuiContainerPieces(container);

        list.add(new BasicRenderedGuiPiece(56, 25, 64, 54,
                ClientProxy.MACHINES_BACKGROUND, 65, 1));

        return list;
    }

    //#endregion

    private int delay = 0;

    @Override
    protected void innerUpdate() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (--this.delay > 0) {
            return;
        }

        int transferred = 0;

        //#region process inputs

        ItemStack stack = this.inputItems.getStackInSlot(0);
        int capacity = this.xpTank.getCapacity() - this.xpTank.getFluidAmount();
        if (!ItemStackUtil.isEmpty(stack) && (capacity > 0)) {
            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                int initial = this.xpTank.getFluidAmount();
                FluidActionResult result = FluidUtil.tryEmptyContainer(stack, this.fluidHandler, Fluid.BUCKET_VOLUME, null, true);
                if (result.isSuccess() && !ItemStack.areItemStacksEqual(result.getResult(), stack)) {
                    this.inputItems.setStackInSlot(0, stack = result.getResult());
                    if (!ItemStackUtil.isEmpty(stack) && this.isEmptyFluidContainer(stack)) {
                        this.inputItems.setStackInSlot(0, this.inputItems.insertItem(1, stack, false));
                    }

                    transferred += (this.xpTank.getFluidAmount() - initial);
                }
            }
        }

        //#endregion

        //#region process outputs

        stack = this.outputItems.getStackInSlot(0);
        int maxDrain = this.xpTank.getFluidAmount();
        if (!ItemStackUtil.isEmpty(stack) && (maxDrain > 0)) {
            if (stack.getItem() == Items.GLASS_BOTTLE) {
                //#region glass bottle -> experience bottle

                if (maxDrain >= 15) {
                    ItemStack existing = this.outputItems.getStackInSlot(1);
                    ItemStack result = ItemStackUtil.getEmptyStack();
                    if (ItemStackUtil.isEmpty(existing)) {
                        result = new ItemStack(Items.EXPERIENCE_BOTTLE, 1);
                    } else if (ItemStackUtil.getSize(existing) < existing.getMaxStackSize()) {
                        result = ItemStackUtil.copyWithSize(existing, ItemStackUtil.getSize(existing) + 1);
                    }

                    if (!ItemStackUtil.isEmpty(result)) {
                        this.outputItems.setStackInSlot(1, result);
                        this.xpTank.drain(15, true);

                        ItemStackUtil.shrink(stack, 1);
                        if (ItemStackUtil.getSize(stack) == 0) {
                            this.outputItems.setStackInSlot(0, ItemStackUtil.getEmptyStack());
                        }

                        transferred += 15;
                    }
                }

                //#endregion
            }
            else if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)/*
                    && ItemStackUtil.isEmpty(this.outputItems.getStackInSlot(1))*/) {
                int toFill = Math.max(maxDrain, Fluid.BUCKET_VOLUME);
                int initial = this.xpTank.getFluidAmount();
                FluidActionResult result = FluidUtil.tryFillContainer(stack, this.fluidHandler, toFill, null, true);
                if (result.isSuccess()) {
                    this.outputItems.setStackInSlot(0, stack = result.getResult());

                    transferred += (initial - this.xpTank.getFluidAmount());
                }

                if (!ItemStackUtil.isEmpty(stack) && !this.isEmptyFluidContainer(stack)) {
                    this.outputItems.setStackInSlot(0, this.outputItems.insertItem(1, stack, false));
                }
            }
        }

        //#endregion

        if (transferred > 0) {
            this.delay = Math.max(5, transferred / 50);
            this.forceSync();
        } else if (this.delay < 0) {
            this.delay = 0;
        }
    }

    private boolean isEmptyFluidContainer(ItemStack stack) {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        return (fluid == null) || (fluid.amount == 0);
    }

    public float getFillPercent() {
        if (this.xpTank == null) {
            return 0.0f;
        }
        return (float)this.xpTank.getFluidAmount() / (float)this.xpTank.getCapacity();
    }
}
