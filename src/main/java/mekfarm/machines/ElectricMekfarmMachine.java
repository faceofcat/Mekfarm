package mekfarm.machines;

import mekfarm.common.IMachineAddon;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.containers.BasicTeslaContainer;
import net.ndrei.teslacorelib.containers.FilteredSlot;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.gui.TiledRenderedGuiPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.tileentities.ElectricMachine;

import java.util.List;

/**
 * Created by CF on 2016-11-04.
 */
public abstract class ElectricMekfarmMachine extends ElectricMachine {
    protected ItemStackHandler inStackHandler;
    protected ItemStackHandler outStackHandler;

    protected ElectricMekfarmMachine(int typeId) {
        super(typeId);
    }

    //#region inventories       methods

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.initializeInputInventory();
        this.initializeOutputInventory();
    }

    protected int getInputSlots() {
        return 3;
    }

    protected void initializeInputInventory() {
        int inputSlots = this.getInputSlots();
        if (inputSlots > 0) {
            this.inStackHandler = new ItemStackHandler(Math.max(0, Math.min(3, inputSlots))) {
                @Override
                protected void onContentsChanged(int slot) {
                    ElectricMekfarmMachine.this.markDirty();
                }
            };
            super.addInventory(new ColoredItemHandler(this.inStackHandler, EnumDyeColor.GREEN, "Input Items", new BoundingRectangle(115, 25, 54, 18)) {
                @Override
                public boolean canInsertItem(int slot, ItemStack stack) {
                    return ElectricMekfarmMachine.this.acceptsInputStack(slot, stack);
                }

                @Override
                public boolean canExtractItem(int slot) {
                    return false;
                }

                @Override
                public List<Slot> getSlots(BasicTeslaContainer container) {
                    List<Slot> slots = super.getSlots(container);

                    BoundingRectangle box = this.getBoundingBox();
                    for (int x = 0; x < this.handler.getSlots(); x++) {
                        slots.add(new FilteredSlot(this.getItemHandlerForContainer(), x, box.getLeft() + 1 + x * 18, box.getTop() + 1));
                    }

                    return slots;
                }

                @Override
                public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
                    List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);

                    BoundingRectangle box = this.getBoundingBox();
                    pieces.add(new TiledRenderedGuiPiece(box.getLeft(), box.getTop(), 18, 18,
                            this.handler.getSlots(), 1,
                            BasicTeslaGuiContainer.MACHINE_BACKGROUND, 108, 225, EnumDyeColor.GREEN));

                    return pieces;
                }
            });
        }
        else {
            this.inStackHandler = null;
        }
    }

    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        return true;
    }

    protected int getOutputSlots() {
        return 6;
    }

    protected void initializeOutputInventory() {
        int outputSlots = this.getOutputSlots();
        if (outputSlots > 0) {
            this.outStackHandler = new ItemStackHandler(Math.max(0, Math.min(6, outputSlots))) {
                @Override
                protected void onContentsChanged(int slot) {
                    ElectricMekfarmMachine.this.markDirty();
                }
            };
            int columns = Math.min(3, this.outStackHandler.getSlots());
            int rows = Math.min(2, this.outStackHandler.getSlots() / columns);
            super.addInventory(new ColoredItemHandler(this.outStackHandler, EnumDyeColor.PURPLE, "Output Items", new BoundingRectangle(115, 43, columns * 18, rows * 18)) {
                @Override
                public boolean canInsertItem(int slot, ItemStack stack) {
                    return false;
                }

                @Override
                public boolean canExtractItem(int slot) {
                    return true;
                }

                @Override
                public List<Slot> getSlots(BasicTeslaContainer container) {
                    List<Slot> slots = super.getSlots(container);

                    BoundingRectangle box = this.getBoundingBox();
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 2; y++) {
                            int i = y * 3 + x;
                            if (i >= this.handler.getSlots()) {
                                continue;
                            }

                            slots.add(new FilteredSlot(this.getItemHandlerForContainer(), i,
                                    box.getLeft() + 1 + x * 18, box.getTop() + 1 + y * 18));
                        }
                    }

                    return slots;
                }

                @Override
                public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
                    List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);

                    BoundingRectangle box = this.getBoundingBox();
                    pieces.add(new TiledRenderedGuiPiece(box.getLeft(), box.getTop(), 18, 18,
                            Math.min(3, this.handler.getSlots()),
                            Math.min(2, this.handler.getSlots() / Math.min(3, this.handler.getSlots())),
                            BasicTeslaGuiContainer.MACHINE_BACKGROUND, 108, 225, EnumDyeColor.PURPLE));

                    return pieces;
                }
            });
        }
        else {
            this.outStackHandler = null;
        }
    }

    @Override
    protected boolean isValidAddonItem(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() instanceof IMachineAddon)) {
            return ((IMachineAddon) stack.getItem()).canBeAddedTo(this);
        }
        return false;
    }

    //#endregion
    //#region write/read/sync   methods

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("income")) {
            this.inStackHandler.deserializeNBT(compound.getCompoundTag("income"));
        }
        if (compound.hasKey("outcome")) {
            this.outStackHandler.deserializeNBT(compound.getCompoundTag("outcome"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        if (this.inStackHandler != null) {
            compound.setTag("income", this.inStackHandler.serializeNBT());
        }
        if (this.outStackHandler != null) {
            compound.setTag("outcome", this.outStackHandler.serializeNBT());
        }

        return compound;
    }

    //#endregion
}
