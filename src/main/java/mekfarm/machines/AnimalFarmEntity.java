package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import mekfarm.containers.AnimalFarmContainer;
import mekfarm.items.AnimalPackageItem;
import mekfarm.items.BaseAnimalFilterItem;
import mekfarm.ui.FarmContainerGUI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class AnimalFarmEntity extends BaseElectricEntity<AnimalFarmContainer, FarmContainerGUI> {
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
    }

    private final float ENERGY_PACKAGE = .9f;
    private final float ENERGY_FEED = .1f;
    private final float ENERGY_SHEAR = .3f;
    private final float ENERGY_MILK = .3f;

    public AnimalFarmEntity() {
        super(1, 500000, 3, 6, 1, AnimalFarmContainer.class, FarmContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        // test for animal package
        if (stack.getItem().getRegistryName().equals(ItemsRegistry.animalPackage.getRegistryName())) {
            return (false == ItemsRegistry.animalPackage.hasAnimal(stack));
        }

        return AnimalFarmEntity.foodItems.contains(stack.getItem().getRegistryName().toString());
    }

    @Override
    protected float performWork() {
        List<EntityAnimal> breedable = Lists.newArrayList();
        float result = 0.0f;

        //region find animals

        EnumFacing facing = BlocksRegistry.animalFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(AnimalFarmBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        // find animal
        List<EntityAnimal> animals = this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, aabb);
        List<IShearable> shearables = Lists.newArrayList();
        List<EntityCow> adultCows = Lists.newArrayList();
        ItemStack filterStack = this.filtersHandler.getStackInSlot(0, true);
        BaseAnimalFilterItem filter = ((filterStack != null) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                ? (BaseAnimalFilterItem) filterStack.getItem()
                : null;
        EntityAnimal animalToPackage = null;
        if ((animals != null) && (animals.size() > 0)) {
            for(int i = 0; i < animals.size(); i++) {
                EntityAnimal thingy = animals.get(i);

                if ((animalToPackage == null) && ((filter == null) || filter.shouldHandle(thingy))) {
                    animalToPackage = thingy;
                }
                else if (this.canBreed(thingy)) {
                    breedable.add(thingy);
                }

                if (thingy instanceof IShearable) {
                    shearables.add((IShearable)thingy);
                }

                if (thingy instanceof EntityCow) {
                    EntityCow cow = (EntityCow)thingy;
                    if (!cow.isChild()) {
                        adultCows.add(cow);
                    }
                }
            }
        }

        //endregion

        //region process package

        if (animalToPackage != null) {
            ItemStack packageStack = null;
            int packageSlot = 0;
            for(int ti = 0; ti < this.inStackHandler.getSlots(); ti++) {
                packageStack = this.inStackHandler.extractItem(ti, 1, true, true);
                if ((packageStack != null) && (packageStack.stackSize > 0) && (packageStack.getItem() instanceof AnimalPackageItem) && !ItemsRegistry.animalPackage.hasAnimal(packageStack)) {
                    packageSlot = ti;
                    break;
                }
                packageStack = null;
            }
            if (packageStack != null) {
                ItemStack stackCopy = packageStack.copy();

                stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
                NBTTagCompound animalCompound = new NBTTagCompound();
                animalToPackage.writeEntityToNBT(animalCompound);
                stackCopy.setTagInfo("animal", animalCompound);
                stackCopy.setTagInfo("animalClass", new NBTTagString(animalToPackage.getClass().getName()));

                ItemStack finalStack = this.outStackHandler.insertItems(stackCopy, false);
                int inserted = packageStack.stackSize - ((finalStack == null) ? 0 : finalStack.stackSize);
                if (inserted > 0) {
                    this.getWorld().removeEntity(animalToPackage);
                    this.inStackHandler.extractItem(packageSlot, inserted, false, true);
                    animalToPackage = null;
                    result += ENERGY_PACKAGE;
                }
            }
        }

        if ((animalToPackage != null) && this.canBreed(animalToPackage)) {
            breedable.add(animalToPackage);
        }

        //endregion

        //region process food

        if ((breedable.size() >= 2) && ((1.0f - result) >= ENERGY_FEED)) {
            ItemStack food0 = this.inStackHandler.extractItem(0, 2, true, true);
            ItemStack food1 = this.inStackHandler.extractItem(1, 2, true, true);
            ItemStack food2 = this.inStackHandler.extractItem(2, 2, true, true);
            if ((food0 != null) || (food1 != null) || (food2 != null)) {
                for (int i = 0; i < breedable.size(); i++) {
                    boolean breed = false;
                    for (int j = i + 1; j < breedable.size(); j++) {
                        EntityAnimal a = breedable.get(i);
                        EntityAnimal b = breedable.get(j);
                        if ((a != null) && (b != null) && (a.getClass().equals(b.getClass()))) {
                            int slotA = -1;
                            int slotB = -1;
                            int size0 = (food0 != null) ? food0.stackSize : 0;
                            int size1 = (food1 != null) ? food1.stackSize : 0;
                            int size2 = (food2 != null) ? food2.stackSize : 0;

                            if ((size0 > 0) && a.isBreedingItem(food0)) {
                                slotA = 0;
                                size0--;
                            }
                            else if ((size1 > 0) && a.isBreedingItem(food1)) {
                                slotA = 1;
                                size1--;
                            }
                            else if ((size2 > 0) && a.isBreedingItem(food2)) {
                                slotA = 2;
                                size2--;
                            }

                            if ((size0 > 0) && b.isBreedingItem(food0)) {
                                slotB = 0;
//                                size1--;
                            }
                            else if ((size1 > 0) && b.isBreedingItem(food1)) {
                                slotB = 1;
//                                size1--;
                            }
                            else if ((size2 > 0) && b.isBreedingItem(food2)) {
                                slotB = 2;
//                                size2--;
                            }

                            if ((slotA >= 0) && (slotB >= 0)) {
                                // food found!
                                this.inStackHandler.extractItem(slotA, 1, false, true);
                                this.inStackHandler.extractItem(slotB, 1, false, true);
                                a.setInLove(MekfarmMod.getFakePlayer(this.getWorld()));
                                b.setInLove(MekfarmMod.getFakePlayer(this.getWorld()));
                                result += ENERGY_FEED;
                                breed = true;
                                break;
                            }
                        }
                    }
                    if (breed) {
                        break;
                    }
                }
            }
        }

        //endregion

        //region process shears

        if ((shearables.size() > 0) && ((1 - result) >= ENERGY_SHEAR)) {
            for(int i = 0; i < this.inStackHandler.getSlots(); i++) {
                ItemStack stack = this.inStackHandler.getStackInSlot(i, true);
                if ((stack == null) || !stack.getItem().getRegistryName().equals(Items.SHEARS.getRegistryName())) {
                    continue;
                }

                for(IShearable shearable : shearables) {
                    if ((shearable != null) && shearable.isShearable(stack, this.getWorld(), this.getPos())) {
                        List<ItemStack> loot = shearable.onSheared(stack, this.getWorld(), this.getPos(), 0);
                        if ((loot != null) && (loot.size() > 0)) {
                            for(int j = 0; j < loot.size(); j++) {
                                ItemStack stillThere = this.outStackHandler.insertItems(loot.get(j), false);
                                if ((stillThere != null) && (stillThere.stackSize > 0)) {
                                    BlockPos pos = ((Entity)shearable).getPosition();
                                    this.getWorld().spawnEntityInWorld(new EntityItem(this.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stillThere));
                                }
                            }

                            if (stack.attemptDamageItem(1, this.getWorld().rand)) {
                                this.inStackHandler.setStackInSlot(i, null);
                            }

                            result += ENERGY_SHEAR;
                            break;
                        }
                    }
                }

                break;
            }
        }

        //endregion

        //region process bucket

        if ((adultCows.size() > 0) && ((1 - result) > ENERGY_MILK)) {
            for(int i = 0; i < this.inStackHandler.getSlots(); i++) {
                ItemStack stack = this.inStackHandler.extractItem(i, 1, true, true);
                if ((stack != null) && (stack.stackSize == 1) && stack.getItem().getRegistryName().equals(Items.BUCKET.getRegistryName())) {
                    ItemStack milk = new ItemStack(Items.MILK_BUCKET, 1);
                    milk = this.outStackHandler.insertItems(milk, false);
                    if ((milk == null) || (milk.stackSize == 0)) {
                        this.inStackHandler.extractItem(i, 1, false, true);

                        result += ENERGY_MILK;
                        break;
                    }
                }
            }
        }

        //endregion

        //region process bowl

        if ((adultCows.size() > 0) && ((1 - result) > ENERGY_MILK)) {
            int mooshrooms = 0;
            for(EntityCow cow: adultCows) {
                if (cow instanceof EntityMooshroom) {
                    mooshrooms++;
                }
            }
            if (mooshrooms > 0) {
                for (int i = 0; i < this.inStackHandler.getSlots(); i++) {
                    ItemStack stack = this.inStackHandler.extractItem(i, 1, true, true);
                    if ((stack != null) && (stack.stackSize == 1) && stack.getItem().getRegistryName().equals(Items.BOWL.getRegistryName())) {
                        ItemStack stew = new ItemStack(Items.MUSHROOM_STEW, 1);
                        stew = this.outStackHandler.insertItems(stew, false);
                        if ((stew == null) || (stew.stackSize == 0)) {
                            this.inStackHandler.extractItem(i, 1, false, true);

                            result += ENERGY_MILK;
                            break;
                        }
                    }
                }
            }
        }

        //endregion

        return result;
    }

    private boolean canBreed(EntityAnimal animal) {
        return ((animal != null) && !animal.isInLove() && !animal.isChild() && (animal.getGrowingAge() == 0));
    }
}
