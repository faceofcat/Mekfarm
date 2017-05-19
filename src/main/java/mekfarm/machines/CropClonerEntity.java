package mekfarm.machines;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;
import net.ndrei.teslacorelib.capabilities.hud.HudInfoLine;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

import java.awt.*;
import java.util.List;

/**
 * Created by CF on 2016-11-24.
 */
public class CropClonerEntity extends ElectricMekfarmMachine {
    private IBlockState plantedThing = null;
    private IFluidTank waterTank;

    public CropClonerEntity() {
        super(CropClonerEntity.class.getName().hashCode());
    }

    @Override
    public boolean supportsRangeAddons() {
        return false;
    }

    @Override
    protected void createAddonsInventory() {
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.waterTank = super.addFluidTank(FluidRegistry.WATER, 5000, EnumDyeColor.BLUE, "Water Tank",
                new BoundingRectangle(43, 25, 18, 54));
    }

    @Override
    protected int getInputSlots() {
        return 1;
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack)) {
            return false;
        }

        if (stack.getItem() instanceof IPlantable) {
            IPlantable plant = (IPlantable) stack.getItem();
            if (plant.getPlantType(this.getWorld(), this.getPos()) == EnumPlantType.Crop) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected float performWork() {
        float result = 0.0f;
        // EnumFacing facing = super.getFacing();

        if (this.plantedThing != null) {
            PropertyInteger ageProperty = this.getAgeProperty(this.plantedThing);
            if (ageProperty != null) {
                int age = this.plantedThing.getValue(ageProperty);
                Integer[] ages = ageProperty.getAllowedValues().toArray(new Integer[0]);
                if (age == ages[ages.length - 1]) {
                    List<ItemStack> stacks = this.plantedThing.getBlock().getDrops(this.getWorld(), this.getPos(), this.plantedThing, 0);
//                    if (stacks != null) {
//                        for (ItemStack s : stacks) {
//                            ItemStack remaining = ItemHandlerHelper.insertItem(this.outStackHandler, s, false);
//                            if (!ItemStackUtil.isEmpty(remaining)) {
////                                BlockPos spawnPos = this.pos.offset(facing);
////                                world.spawnEntity(new EntityItem(this.getWorld(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), remaining));
//                                super.spawnOverloadedItem(remaining);
//                            }
//                        }
//                    }
                    if (super.outputItems(stacks)) {
                        this.plantedThing = null;
                        result += .85f;
                    }
                } else {
                    this.plantedThing = this.plantedThing.withProperty(ageProperty, ++age);
                    result += .85f;
                }
                this.onPlantedThingChanged();
            }
//            result += .85f;
        }

        if ((this.plantedThing == null) && (this.waterTank != null) && (this.waterTank.getFluidAmount() >= 250)) {
            ItemStack stack = this.inStackHandler.getStackInSlot(0);
            if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() instanceof IPlantable)) {
                IPlantable plantable = (IPlantable) stack.getItem();
                if (plantable.getPlantType(this.getWorld(), this.getPos()) == EnumPlantType.Crop) {
                    this.plantedThing = plantable.getPlant(this.getWorld(), this.getPos());
                    this.waterTank.drain(250, true); // TODO: <-- do this better
                    this.onPlantedThingChanged();
                }
            }
            result += .15f;
        }

        return result;
    }

    private void onPlantedThingChanged() {
        if ((null != this.getWorld()) && (null != this.getPos())) { // <-- weird, but it actually happens!!
            int state = (this.plantedThing == null) ? 0 : 1;
            IBlockState block = this.getWorld().getBlockState(this.getPos());
            if (block.getValue(CropClonerBlock.STATE) != state) {
                CropClonerBlock.setState(block.withProperty(CropClonerBlock.STATE, state), this.getWorld(), this.getPos());
            }
        }

        this.markDirty();
        this.forceSync();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("plantDomain") && compound.hasKey("plantPath")) {
            ResourceLocation location = new ResourceLocation(
                    compound.getString("plantDomain"),
                    compound.getString("plantPath"));
            Block block = Block.REGISTRY.getObject(location);
            if (block != null) {
                this.plantedThing = block.getDefaultState();
                this.onPlantedThingChanged();
            }
        }

        if (compound.hasKey("plantAge") && (this.plantedThing != null)) {
            int age = compound.getInteger("plantAge");
            PropertyInteger ageProperty = this.getAgeProperty(this.plantedThing);
            if (ageProperty != null) {
                this.plantedThing = this.plantedThing.withProperty(ageProperty, age);
                this.onPlantedThingChanged();
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        if (this.plantedThing != null) {
            ResourceLocation resource = this.plantedThing.getBlock().getRegistryName();
            compound.setString("plantDomain", resource.getResourceDomain());
            compound.setString("plantPath", resource.getResourcePath());
            PropertyInteger ageProperty = this.getAgeProperty(this.plantedThing);
            if (ageProperty != null) {
                compound.setInteger("plantAge", this.plantedThing.getValue(ageProperty));
            }
        }

        return compound;
    }

    private PropertyInteger getAgeProperty(IBlockState thing) {
        if (thing != null) {
            for (IProperty p : thing.getPropertyNames()) {
                if ((p instanceof PropertyInteger) && (p.getName() == "age")) {
                    return (PropertyInteger) p;
                }
            }
        }
        return null;
    }

    public IBlockState getPlantedThing() {
        return this.plantedThing;
    }

    public List<HudInfoLine> getHUDLines() {
        List<HudInfoLine> list = super.getHUDLines();
        if (list == null)
            list = Lists.newArrayList();

        if (this.plantedThing == null) {
            list.add(new HudInfoLine(new Color(255, 159, 51),
                    new Color(255, 159, 51, 42),
                    "no seed")
                    .setTextAlignment(HudInfoLine.TextAlignment.CENTER));
        } else /*if (this.plantedThing != null)*/ {
            list.add(new HudInfoLine(Color.WHITE, this.plantedThing.getBlock().getLocalizedName())
                    .setTextAlignment(HudInfoLine.TextAlignment.CENTER));
            PropertyInteger age = this.getAgeProperty(this.plantedThing);
            if (age != null) {
                int percent = (this.plantedThing.getValue(age) * 100) / age.getAllowedValues().size();
                list.add(new HudInfoLine(Color.CYAN,
                        new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 192),
                        new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), 192),
                        "growth: " + percent + "%")
                        .setProgress((float)percent / 100.0f, new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), 50)));
            }
        }

        return list;
    }
}