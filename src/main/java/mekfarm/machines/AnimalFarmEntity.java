package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import mekfarm.containers.FarmContainer;
import mekfarm.ui.AnimalFarmContainerGUI;
import mekfarm.items.AnimalPackageItem;
import mekfarm.items.BaseAnimalFilterItem;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class AnimalFarmEntity extends BaseElectricEntity<FarmContainer, AnimalFarmContainerGUI> {
    private static ArrayList<String> foodItems = new ArrayList<>();

    static {
        AnimalFarmEntity.foodItems.add("minecraft:shears");
        // ^^ not really food :D

        AnimalFarmEntity.foodItems.add("minecraft:wheat");
        AnimalFarmEntity.foodItems.add("minecraft:carrot");
        AnimalFarmEntity.foodItems.add("minecraft:potato");
        AnimalFarmEntity.foodItems.add("minecraft:wheat_seeds");
    }

    public AnimalFarmEntity() {
        super(1, 500000, 3, 6, 1, FarmContainer.class, AnimalFarmContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        if (slot == 0) {
            // test for animal package
            if (stack.getItem().getRegistryName().equals(ItemsRegistry.animalPackage.getRegistryName())) {
                return (false == ItemsRegistry.animalPackage.hasAnimal(stack));
            }
        }
        else if ((slot == 1) || (slot == 2)) {
            // test for food
            if (AnimalFarmEntity.foodItems.contains(stack.getItem().getRegistryName().toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected float performWork() {
        List<EntityAnimal> breedable = Lists.newArrayList();
        float result = 0.0f;

        //region find animals

        EnumFacing facing = BlocksRegistry.animalFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(BlocksRegistry.animalFarmBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        // find animal
        List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, aabb);
        ItemStack filterStack = this.filtersHandler.getStackInSlot(0, true);
        BaseAnimalFilterItem filter = ((filterStack != null) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                ? (BaseAnimalFilterItem) filterStack.getItem()
                : null;
        EntityAnimal animalToPackage = null;
        if ((list != null) && (list.size() > 0)) {
            for(int i = 0; i < list.size(); i++) {
                EntityAnimal thingy = list.get(i);

                if ((animalToPackage == null) && ((filter == null) || filter.shouldHandle(thingy))) {
                    animalToPackage = thingy;
                }
                else if (this.canBreed(thingy) == true) {
                    breedable.add(thingy);
                }
            }
        }

        //endregion

        //region process package

        if (animalToPackage != null) {
            ItemStack packageStack = this.inStackHandler.extractItem(0, 1, true, true);
            if ((packageStack != null) && (packageStack.stackSize > 0)) {
                ItemStack stackCopy = packageStack.copy();

                if ((stackCopy.getItem() instanceof AnimalPackageItem) && !ItemsRegistry.animalPackage.hasAnimal(stackCopy)) {
                    stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
                    NBTTagCompound animalCompound = new NBTTagCompound();
                    animalToPackage.writeEntityToNBT(animalCompound);
                    stackCopy.setTagInfo("animal", animalCompound);
                    stackCopy.setTagInfo("animalClass", new NBTTagString(animalToPackage.getClass().getName()));

                    ItemStack finalStack = this.outStackHandler.insertItems(stackCopy, false);
                    int inserted = packageStack.stackSize - ((finalStack == null) ? 0 : finalStack.stackSize);
                    if (inserted > 0) {
                        this.worldObj.removeEntity(animalToPackage);
                        this.inStackHandler.extractItem(0, inserted, false, true);
                        animalToPackage = null;
                        result += 0.9f;
                    }
                }
            }
        }

        if ((animalToPackage != null) && this.canBreed(animalToPackage)) {
            breedable.add(animalToPackage);
        }

        //endregion

        //region process food

        if (breedable.size() >= 2) {
            ItemStack food1 = this.inStackHandler.extractItem(1, 2, true, true);
            ItemStack food2 = this.inStackHandler.extractItem(2, 2, true, true);
            if ((food1 != null) || (food2 != null)) {
                for (int i = 0; i < breedable.size(); i++) {
                    boolean breed = false;
                    for (int j = i + 1; j < breedable.size(); j++) {
                        EntityAnimal a = breedable.get(i);
                        EntityAnimal b = breedable.get(j);
                        if ((a != null) && (b != null) && (a.getClass().equals(b.getClass()))) {
                            int slotA = -1;
                            int slotB = -1;
                            int size1 = (food1 != null) ? food1.stackSize : 0;
                            int size2 = (food2 != null) ? food2.stackSize : 0;

                            if ((size1 > 0) && a.isBreedingItem(food1)) {
                                slotA = 1;
                                size1--;
                            }
                            else if ((size2 > 0) && a.isBreedingItem(food2)) {
                                slotA = 2;
                                size2--;
                            }

                            if ((size1 > 0) && b.isBreedingItem(food1)) {
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
                                result += .1f;
                                breed = true;
                                break;
                            }
                        }
                    }
                    if (breed == true) {
                        break;
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
