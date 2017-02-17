package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.client.AnimalGymInfoPiece;
import mekfarm.common.ItemsRegistry;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.TeslaCoreLib;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.containers.BasicTeslaContainer;
import net.ndrei.teslacorelib.containers.FilteredSlot;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.ButtonPiece;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.gui.TiledRenderedGuiPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.netsync.SimpleNBTMessage;
import net.ndrei.teslacorelib.tileentities.ElectricGenerator;

import java.util.List;

/**
 * Created by CF on 2017-01-15.
 */
public class AnimalGymEntity extends ElectricGenerator {
    protected ItemStackHandler inStackHandler;
    protected ItemStackHandler outStackHandler;

    public AnimalGymEntity() {
        super(AnimalGymEntity.class.hashCode());
    }

    //#region inventories       methods

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.inStackHandler = new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                AnimalGymEntity.this.markDirty();
            }
        };
        super.addInventory(new ColoredItemHandler(this.inStackHandler, EnumDyeColor.GREEN, "Input Items", new BoundingRectangle(61, 25, 18, 54)) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return AnimalGymEntity.this.acceptsInputStack(slot, stack);
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }

            @Override
            public List<Slot> getSlots(BasicTeslaContainer container) {
                List<Slot> slots = super.getSlots(container);

                BoundingRectangle box = this.getBoundingBox();
                for(int y = 0; y < this.handler.getSlots(); y++) {
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), y, box.getLeft() + 1, box.getTop() + 1 + y * 18));
                }

                return slots;
            }

            @Override
            public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
                List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);

                BoundingRectangle box = this.getBoundingBox();
                pieces.add(new TiledRenderedGuiPiece(box.getLeft(), box.getTop(), 18, 18,
                        1, 3,
                        BasicTeslaGuiContainer.MACHINE_BACKGROUND, 108, 225, EnumDyeColor.GREEN));

                return pieces;
            }
        });
        super.addInventoryToStorage(this.inStackHandler, "gym_inputs");

        this.outStackHandler = new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                AnimalGymEntity.this.markDirty();
            }
        };
        super.addInventory(new ColoredItemHandler(this.outStackHandler, EnumDyeColor.PURPLE, "Output Items", new BoundingRectangle(151, 25, 18, 54)) {
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
                for(int y = 0; y < 3; y++) {
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), y,
                            box.getLeft() + 1, box.getTop() + 1 + y * 18));
                }

                return slots;
            }

            @Override
            public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
                List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);

                BoundingRectangle box = this.getBoundingBox();
                pieces.add(new TiledRenderedGuiPiece(box.getLeft(), box.getTop(), 18, 18,
                        1, 3,
                        BasicTeslaGuiContainer.MACHINE_BACKGROUND, 108, 225, EnumDyeColor.PURPLE));

                return pieces;
            }
        });
        super.addInventoryToStorage(this.outStackHandler, "gym_outputs");
    }

    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack)) {
            return false;
        }

        if (stack.getItem() != ItemsRegistry.animalPackage) {
            return false;
        }

        return ItemsRegistry.animalPackage.hasAnimal(stack);
    }

    //#endregion
    //#region storage           methods

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("athlete", Constants.NBT.TAG_COMPOUND)
                && compound.hasKey("athleteType", Constants.NBT.TAG_STRING)) {
            String animalType = compound.getString("athleteType");
            try {
                this.currentAnimalClass = Class.forName(animalType);
                this.currentAnimalTag = compound.getCompoundTag("athlete");
//                Object thing = cea.getConstructor(World.class).newInstance(world);
//                if (thing instanceof EntityAnimal) {
//                    NBTTagCompound animalCompound = compound.getCompoundTag("athlete");
//                    if (animalCompound.hasKey("Attributes", 9) && this.getWorld().isRemote)
//                    {
//                        // this is the reverse of what EntityLiving does... so that attributes work on client side
//                        SharedMonsterAttributes.setAttributeModifiers(((EntityAnimal) thing).getAttributeMap(),
//                                animalCompound.getTagList("Attributes", 10));
//                    }
//                    ((EntityAnimal) thing).deserializeNBT(animalCompound);
//                    this.currentAnimal = (EntityAnimal) thing;
//                }
            }
            catch(Throwable t) {
                MekfarmMod.logger.error("Error deserializing animal gym athlete.", t);
                this.currentAnimal = null;
                this.currentAnimalClass = null;
                this.currentAnimalTag = null;
            }
        }
        else {
            this.currentAnimal = null;
            this.currentAnimalClass = null;
            this.currentAnimalTag = null;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        EntityAnimal current = this.getCurrent();
        if (current != null) {
            NBTTagCompound animalCompound = current.writeToNBT(new NBTTagCompound());
            if (animalCompound != null) {
                compound.setString("athleteType", current.getClass().getName());
                compound.setTag("athlete", animalCompound);
            }
        }

        return compound;
    }

    //#endregion
    //#region rendering         methods

    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
        List<IGuiContainerPiece> list = super.getGuiContainerPieces(container);

        list.add(new AnimalGymInfoPiece(this,88, 25));

        list.add(new ButtonPiece(132, 29, 8, 7) {
            @Override
            protected void renderState(BasicTeslaGuiContainer container, boolean over, BoundingRectangle box) {
                if (AnimalGymEntity.this.getCurrent() == null) {
                    return;
                }

                container.mc.getTextureManager().bindTexture(MekfarmMod.MACHINES_BACKGROUND);
                container.drawTexturedRect(box.getLeft() - container.getGuiLeft(), box.getTop() - container.getGuiTop(),
                        56, over ? 9 : 1, 8, 7);
            }

            @Override
            protected void clicked() {
                if (AnimalGymEntity.this.getCurrent() == null) {
                    return;
                }

                NBTTagCompound nbt = AnimalGymEntity.this.setupSpecialNBTMessage("PACKAGE_ITEM");
                TeslaCoreLib.network.sendToServer(new SimpleNBTMessage(AnimalGymEntity.this, nbt));
            }
        });

        return list;
    }

    @Override
    protected SimpleNBTMessage processClientMessage(String messageType, NBTTagCompound compound) {
        if ((messageType != null) && messageType.equals("PACKAGE_ITEM")) {
            this.packageCurrent();
        }

        return super.processClientMessage(messageType, compound);
    }

    //#endregion

    private int teslaPerHeart = 8400;
    private float teslaSpeedMultiplier = 320;

    private Class currentAnimalClass = null;
    private NBTTagCompound currentAnimalTag = null;
    private EntityAnimal currentAnimal = null;

    private EntityAnimal getCurrent() {
        if ((this.currentAnimalClass != null) && (this.currentAnimalTag != null)) {
            try {
                Object thing = this.currentAnimalClass.getConstructor(World.class).newInstance(this.getWorld());
                if (thing instanceof EntityAnimal) {
                    NBTTagCompound animalCompound = this.currentAnimalTag;
                    if (animalCompound.hasKey("Attributes", 9) && this.getWorld().isRemote)
                    {
                        // this is the reverse of what EntityLiving does... so that attributes work on client side
                        SharedMonsterAttributes.setAttributeModifiers(((EntityAnimal) thing).getAttributeMap(),
                                animalCompound.getTagList("Attributes", 10));
                    }
                    ((EntityAnimal) thing).deserializeNBT(animalCompound);
                    this.currentAnimal = (EntityAnimal) thing;
                    this.markDirty();
                }
            }
            catch(Throwable t) {
                MekfarmMod.logger.error("Error deserializing animal gym athlete.", t);
                this.currentAnimal = null;
                this.markDirty();
            }
            finally
            {
                this.currentAnimalClass = null;
                this.currentAnimalTag = null;
                this.markDirty();
            }
        }

        if (this.currentAnimal == null) {
            // try to unpackage a new animal
            for(int index = 0; index < this.inStackHandler.getSlots(); index++) {
                ItemStack stack = this.inStackHandler.getStackInSlot(index);
                if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() == ItemsRegistry.animalPackage)
                        && ItemsRegistry.animalPackage.hasAnimal(stack)) {
                    this.inStackHandler.setStackInSlot(index, ItemStackUtil.getEmptyStack());
                    this.currentAnimal = ItemsRegistry.animalPackage.unpackage(this.getWorld(), stack);
                    this.forceSync();
                    return (this.currentAnimal == null) ? null : this.getCurrent();
                }
            }

            return null;
        }

        return this.currentAnimal;
    }

    private boolean packageCurrent() {
        EntityAnimal ea = this.getCurrent();
        if (ea == null) {
            return false;
        }

        ItemStack remaining = ItemHandlerHelper.insertItem(this.outStackHandler,
                AnimalFarmEntity.packageAnimal(null, ea),
                false);
        if (ItemStackUtil.isEmpty(remaining)) {
            this.currentAnimal = null;
            this.forceSync();
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    protected long consumeFuel() {
        EntityAnimal ea = this.getCurrent();
        if (ea == null) {
            return 0;
        }

        if (ea.getHealth() <= 2.0) {
            // see if the animal is lucky to live
            if (this.getWorld().rand.nextFloat() > .2) {
                // 80% chance of surviving
                if (this.packageCurrent()) {
                    ea = this.getCurrent();
                }
            }
        }

        if (ea == null) {
            return 0;
        }

        ea.setHealth(ea.getHealth() - 1.0f);
        if (ea.getHealth() < 0.001f) {
            // sometimes it happens :S
            this.currentAnimal = null;
            this.forceSync();
            this.markDirty();
            return 0;
        }
        return this.teslaPerHeart;
    }

    protected long getEnergyFillRate() {
        return Math.round(this.teslaSpeedMultiplier * this.getSpeedForCurrent());
    }

    public String getCurrentAnimalType() {
        EntityAnimal a = this.getCurrent();
        return (a == null) ? "n/a" : a.getName(); //.getClass().getSimpleName();
    }

    public EntityAnimal getCurrentAnimal() {
        return this.getCurrent();
    }

    public float getSpeedForCurrent() {
        EntityAnimal a = this.getCurrent();
        return (a == null) ? 0.0f : (float)a.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
    }

    public float getPowerPerTick() {
        return (this.teslaSpeedMultiplier * this.getSpeedForCurrent());
    }

    public float getEnduranceForCurrent() {
        EntityAnimal a = this.getCurrent();
        return (a == null) ? 0.0f : a.getHealth();
    }

    public int getLifespanForCurrent() {
        return Math.round(this.getEnduranceForCurrent() * 60.0f * 20.0f);
    }

    public int getMaxPowerForCurrent() {
        return Math.round(this.teslaPerHeart * this.getEnduranceForCurrent());
    }
}
