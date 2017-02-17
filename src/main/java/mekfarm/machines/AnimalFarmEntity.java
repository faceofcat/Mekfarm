package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.ItemsRegistry;
import mekfarm.items.AnimalPackageItem;
import mekfarm.items.BaseAnimalFilterItem;
import mekfarm.machines.wrappers.AnimalWrapperFactory;
import mekfarm.machines.wrappers.IAnimalWrapper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemHandlerHelper;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class AnimalFarmEntity extends ElectricMekfarmMachine {
    private static ArrayList<Item> foodItems = new ArrayList<>();

    static {
        AnimalFarmEntity.foodItems.add(Items.SHEARS);
        AnimalFarmEntity.foodItems.add(Items.BUCKET);
        AnimalFarmEntity.foodItems.add(Items.BOWL);
        // ^^ not really food :D

        AnimalWrapperFactory.populateFoodItems(AnimalFarmEntity.foodItems);
    }

    private final float ENERGY_PACKAGE = .9f;
    private final float ENERGY_FEED = .1f;
    private final float ENERGY_SHEAR = .3f;
    private final float ENERGY_MILK = .3f;

    public AnimalFarmEntity() {
        super(AnimalFarmEntity.class.hashCode());
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack))
            return true;

        // test for animal package
        if (stack.getItem().getRegistryName().equals(ItemsRegistry.animalPackage.getRegistryName())) {
            return !ItemsRegistry.animalPackage.hasAnimal(stack);
        }

        return AnimalFarmEntity.foodItems.contains(stack.getItem());
    }

    @Override
    protected float performWork() {
        List<IAnimalWrapper> toProcess = Lists.newArrayList();
        IAnimalWrapper animalToPackage = null;
        float result = 0.0f;

        //region find animals

        EnumFacing facing = super.getFacing();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing.getOpposite(), 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        // find animal
        List<EntityAnimal> animals = this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, aabb);
        ItemStack filterStack = this.addonItems.getStackInSlot(0);
        BaseAnimalFilterItem filter = (!ItemStackUtil.isEmpty(filterStack) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                ? (BaseAnimalFilterItem) filterStack.getItem()
                : null;
        if ((animals.size() > 0)) {
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
                packageStack = this.inStackHandler.extractItem(ti, 1, true);
                if (!ItemStackUtil.isEmpty(packageStack) && (packageStack.getItem() instanceof AnimalPackageItem) && !ItemsRegistry.animalPackage.hasAnimal(packageStack)) {
                    packageSlot = ti;
                    break;
                }
                packageStack = null;
            }
            if (!ItemStackUtil.isEmpty(packageStack)) {
                EntityAnimal animal = animalToPackage.getAnimal();
                ItemStack stackCopy = AnimalFarmEntity.packageAnimal(packageStack, animal);

                ItemStack finalStack = ItemHandlerHelper.insertItem(this.outStackHandler, stackCopy, false);
                int inserted = ItemStackUtil.getSize(packageStack) - ItemStackUtil.getSize(finalStack);
                if (inserted > 0) {
                    this.getWorld().removeEntity(animalToPackage.getAnimal());
                    this.inStackHandler.extractItem(packageSlot, inserted, false);
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

                    List<ItemStack> potentialFood = ItemStackUtil.getCombinedInventory(this.inStackHandler);

                    ItemStack foodStack = null;
                    for(int f = 0; f < potentialFood.size(); f++) {
                        ItemStack tempFood = potentialFood.get(f);
                        if (wrapper.isFood(tempFood)) {
                            foodStack = tempFood;
                            break;
                        }
                    }
                    if (!ItemStackUtil.isEmpty(foodStack)) {
                        for(int j = i+1; j < toProcess.size(); j++) {
                            IAnimalWrapper toMateWith = toProcess.get(j);
                            if (toMateWith.breedable() && toMateWith.isFood(foodStack) && wrapper.canMateWith(toMateWith)) {
                                int foodUsed = wrapper.mate(MekfarmMod.getFakePlayer(this.getWorld()), foodStack, toMateWith);
                                if ((foodUsed > 0) && (foodUsed <= ItemStackUtil.getSize(foodStack))) {
                                    ItemStackUtil.extractFromCombinedInventory(this.inStackHandler, foodStack, foodUsed);
                                    ItemStackUtil.shrink(foodStack, foodUsed);
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
                        if (wrapper.canBeShearedWith(this.inStackHandler.getStackInSlot(s))) {
                            shearsSlot = s;
                            break;
                        }
                    }
                    if (shearsSlot >= 0) {
                        ItemStack shears = this.inStackHandler.getStackInSlot(shearsSlot);
                        List<ItemStack> loot = wrapper.shear(shears, 0);
                        if ((loot != null) && (loot.size() > 0)) {
                            for (int j = 0; j < loot.size(); j++) {
                                ItemStack stillThere = ItemHandlerHelper.insertItem(this.outStackHandler, loot.get(j),false);
                                if (!ItemStackUtil.isEmpty(stillThere)) {
                                    BlockPos pos = wrapper.getAnimal().getPosition();
                                    this.getWorld().spawnEntity(new EntityItem(this.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stillThere));
                                }
                            }

                            if (shears.attemptDamageItem(1, this.getWorld().rand)) {
                                this.inStackHandler.setStackInSlot(shearsSlot, ItemStackUtil.getEmptyStack());
                            }

                            result += ENERGY_SHEAR;
                        }
                    }

                    //endregion
                }

                if (wrapper.canBeMilked() && ((1.0f - result) >= ENERGY_MILK)) {
                    //region no milk left behind!

                    for (int b = 0; b < this.inStackHandler.getSlots(); b++) {
                        ItemStack stack = this.inStackHandler.extractItem(b, 1, true);
                        if ((ItemStackUtil.getSize(stack) == 1) && (stack.getItem() == Items.BUCKET)) {
                            ItemStack milk = wrapper.milk();
                            milk = ItemHandlerHelper.insertItem(this.outStackHandler, milk,false);
                            if (ItemStackUtil.isEmpty(milk)) {
                                this.inStackHandler.extractItem(b, 1, false);

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
                        ItemStack stack = this.inStackHandler.extractItem(b, 1, true);
                        if ((ItemStackUtil.getSize(stack) == 1) && (stack.getItem() == Items.BOWL)) {
                            ItemStack stew = wrapper.bowl();
                            stew = ItemHandlerHelper.insertItem(this.outStackHandler, stew,false);
                            if (ItemStackUtil.isEmpty(stew)) {
                                this.inStackHandler.extractItem(b, 1, false);

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

    static ItemStack packageAnimal(ItemStack packageStack, EntityAnimal animal) {
        ItemStack stackCopy = (packageStack == null) ? new ItemStack(ItemsRegistry.animalPackage) : packageStack.copy();

        stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
        NBTTagCompound animalCompound = new NBTTagCompound();
        animal.writeEntityToNBT(animalCompound);
        stackCopy.setTagInfo("animal", animalCompound);
        stackCopy.setTagInfo("animalClass", new NBTTagString(animal.getClass().getName()));
        stackCopy.setTagInfo("animalHealth", new NBTTagFloat(animal.getHealth()));
        return stackCopy;
    }
}
