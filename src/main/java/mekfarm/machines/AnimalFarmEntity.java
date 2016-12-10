package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.*;
import mekfarm.containers.AnimalFarmContainer;
import mekfarm.items.AnimalPackageItem;
import mekfarm.items.BaseAnimalFilterItem;
import mekfarm.machines.wrappers.AnimalWrapperFactory;
import mekfarm.machines.wrappers.IAnimalWrapper;
import mekfarm.ui.FarmContainerGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class AnimalFarmEntity extends BaseElectricEntity<AnimalFarmContainer> {
    private static ArrayList<String> foodItems = new ArrayList<>();

    static {
        AnimalFarmEntity.foodItems.add("minecraft:shears");
        AnimalFarmEntity.foodItems.add("minecraft:bucket");
        AnimalFarmEntity.foodItems.add("minecraft:bowl");
        // ^^ not really food :D

        AnimalFarmEntity.foodItems.add("minecraft:wheat");
        AnimalFarmEntity.foodItems.add("minecraft:carrot");
        AnimalFarmEntity.foodItems.add("minecraft:potato");
        AnimalFarmEntity.foodItems.add("minecraft:wheat_seeds");
        AnimalFarmEntity.foodItems.add("minecraft:beetroot");
        AnimalFarmEntity.foodItems.add("minecraft:beetroot_seeds");
        AnimalFarmEntity.foodItems.add("minecraft:golden_carrot");
        AnimalFarmEntity.foodItems.add("minecraft:hay_block");
        AnimalFarmEntity.foodItems.add("minecraft:apple");
    }

    private final float ENERGY_PACKAGE = .9f;
    private final float ENERGY_FEED = .1f;
    private final float ENERGY_SHEAR = .3f;
    private final float ENERGY_MILK = .3f;

    public AnimalFarmEntity() {
        super(1, 500000, 3, 6, 1, AnimalFarmContainer.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getContainerGUI(IInventory playerInventory) {
        return new FarmContainerGUI(this, this.getContainer(playerInventory));
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if ((stack == null) || stack.isEmpty())
            return true;

        // test for animal package
        if (stack.getItem().getRegistryName().equals(ItemsRegistry.animalPackage.getRegistryName())) {
            return (false == ItemsRegistry.animalPackage.hasAnimal(stack));
        }

        return AnimalFarmEntity.foodItems.contains(stack.getItem().getRegistryName().toString());
    }

    @Override
    protected float performWork() {
        List<IAnimalWrapper> toProcess = Lists.newArrayList();
        IAnimalWrapper animalToPackage = null;
        float result = 0.0f;

        //region find animals

        EnumFacing facing = BlocksRegistry.animalFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(AnimalFarmBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        // find animal
        List<EntityAnimal> animals = this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, aabb);
        ItemStack filterStack = this.filtersHandler.getStackInSlot(0, true);
        BaseAnimalFilterItem filter = ((filterStack != null) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                ? (BaseAnimalFilterItem) filterStack.getItem()
                : null;
        if ((animals != null) && (animals.size() > 0)) {
            for (int i = 0; i < animals.size(); i++) {
                IAnimalWrapper thingy = AnimalWrapperFactory.getAnimalWrapper(animals.get(i));

                if ((animalToPackage == null) && ((filter == null) || filter.canProcess(this, i, thingy.getAnimal()))) {
                    animalToPackage = thingy;
                } else if (thingy.breedable()) {
                    toProcess.add(thingy);
                }
            }
        }

        //endregion

        //region process package

        if (animalToPackage != null) {
            ItemStack packageStack = null;
            int packageSlot = 0;
            for (int ti = 0; ti < this.inStackHandler.getSlots(); ti++) {
                packageStack = this.inStackHandler.extractItem(ti, 1, true, true);
                if ((packageStack != null) && (packageStack.getCount() > 0) && (packageStack.getItem() instanceof AnimalPackageItem) && !ItemsRegistry.animalPackage.hasAnimal(packageStack)) {
                    packageSlot = ti;
                    break;
                }
                packageStack = null;
            }
            if (packageStack != null) {
                ItemStack stackCopy = packageStack.copy();

                stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
                NBTTagCompound animalCompound = new NBTTagCompound();
                animalToPackage.getAnimal().writeEntityToNBT(animalCompound);
                stackCopy.setTagInfo("animal", animalCompound);
                stackCopy.setTagInfo("animalClass", new NBTTagString(animalToPackage.getAnimal().getClass().getName()));

                ItemStack finalStack = this.outStackHandler.distributeItems(stackCopy, false);
                int inserted = packageStack.getCount() - ((finalStack == null) ? 0 : finalStack.getCount());
                if (inserted > 0) {
                    this.getWorld().removeEntity(animalToPackage.getAnimal());
                    this.inStackHandler.extractItem(packageSlot, inserted, false, true);
                    animalToPackage = null;
                    result += ENERGY_PACKAGE;
                }
            }
        }

        if (animalToPackage != null) {
            toProcess.add(animalToPackage);
            animalToPackage = null;
        }

        //endregion

        float minEnergy = Math.min(ENERGY_FEED, Math.min(ENERGY_MILK, ENERGY_SHEAR));
        if ((toProcess.size() >= 2) && ((1.0f - result) >= minEnergy)) {
            for(int i = 0; i < toProcess.size(); i++) {
                IAnimalWrapper wrapper = toProcess.get(i);
                if (wrapper.breedable() && ((1.0f - result) >= ENERGY_FEED)) {
                    //region breed this thing

                    List<ItemStack> potentialFood = this.inStackHandler.getCombinedInventory();

                    ItemStack foodStack = null;
                    for(int f = 0; f < potentialFood.size(); f++) {
                        ItemStack tempFood = potentialFood.get(f);
                        if (wrapper.isFood(tempFood)) {
                            foodStack = tempFood;
                            break;
                        }
                    }
                    if ((foodStack != null) && !foodStack.isEmpty()) {
                        for(int j = i+1; j < toProcess.size(); j++) {
                            IAnimalWrapper toMateWith = toProcess.get(j);
                            if (toMateWith.breedable() && toMateWith.isFood(foodStack) && wrapper.canMateWith(toMateWith)) {
                                int foodUsed = wrapper.mate(MekfarmMod.getFakePlayer(this.getWorld()), foodStack, toMateWith);
                                if ((foodUsed > 0) && (foodUsed <= foodStack.getCount())) {
                                    this.inStackHandler.extractFromCombinedInventory(foodStack, foodUsed);
                                    foodStack.shrink(foodUsed);
                                    result += ENERGY_FEED;
                                    break;
                                }
                            }
                        }
                    }

                    //endregion
                }

                if (wrapper.shearable() && ((1.0f - result) >= ENERGY_SHEAR)) {
                    //region shear this unfortunate animal

                    int shearsSlot = -1;
                    for(int s = 0; s < this.inStackHandler.getSlots(); s++) {
                        if (wrapper.canBeShearedWith(this.inStackHandler.getStackInSlot(s, true))) {
                            shearsSlot = s;
                            break;
                        }
                    }
                    if (shearsSlot >= 0) {
                        ItemStack shears = this.inStackHandler.getStackInSlot(shearsSlot, true);
                        List<ItemStack> loot = wrapper.shear(shears, 0);
                        if ((loot != null) && (loot.size() > 0)) {
                            for (int j = 0; j < loot.size(); j++) {
                                ItemStack stillThere = this.outStackHandler.distributeItems(loot.get(j), false);
                                if ((stillThere != null) && (stillThere.getCount() > 0)) {
                                    BlockPos pos = wrapper.getAnimal().getPosition();
                                    this.getWorld().spawnEntity(new EntityItem(this.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stillThere));
                                }
                            }

                            if (shears.attemptDamageItem(1, this.getWorld().rand)) {
                                this.inStackHandler.setStackInSlot(shearsSlot, ItemStack.EMPTY);
                            }

                            result += ENERGY_SHEAR;
                        }
                    }

                    //endregion
                }

                if (wrapper.canBeMilked() && ((1.0f - result) >= ENERGY_MILK)) {
                    //region no milk left behind!

                    for (int b = 0; b < this.inStackHandler.getSlots(); b++) {
                        ItemStack stack = this.inStackHandler.extractItem(b, 1, true, true);
                        if ((stack != null) && (stack.getCount() == 1) && (stack.getItem() == Items.BUCKET)) {
                            ItemStack milk = wrapper.milk();
                            milk = this.outStackHandler.distributeItems(milk, false);
                            if ((milk == null) || (milk.getCount() == 0) || milk.isEmpty()) {
                                this.inStackHandler.extractItem(i, 1, false, true);

                                result += ENERGY_MILK;
                                break;
                            }
                        }
                    }

                    //endregion
                }

                //region mushroom stew best stew

                if (wrapper.canBeBowled() && ((1.0f - result) >= ENERGY_MILK)) {
                    for (int b = 0; b < this.inStackHandler.getSlots(); b++) {
                        ItemStack stack = this.inStackHandler.extractItem(b, 1, true, true);
                        if ((stack != null) && (stack.getCount() == 1) && (stack.getItem() == Items.BOWL)) {
                            ItemStack stew = wrapper.bowl();
                            stew = this.outStackHandler.distributeItems(stew, false);
                            if ((stew == null) || (stew.getCount() == 0)) {
                                this.inStackHandler.extractItem(i, 1, false, true);

                                result += ENERGY_MILK;
                                break;
                            }
                        }
                    }
                }

                //endregion

                if ((1.0f - result) < minEnergy) {
                    break; // no more energy
                }
            }
        }

        return result;
    }
}
