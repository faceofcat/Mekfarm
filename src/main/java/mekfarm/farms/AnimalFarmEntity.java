package mekfarm.farms;

import mekfarm.common.BaseElectricEntity;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import mekfarm.containers.FarmContainer;
import mekfarm.containers.FarmContainerGUI;
import mekfarm.items.AnimalPackageItem;
import mekfarm.items.BaseAnimalFilterItem;
import net.minecraft.entity.passive.*;
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
public class AnimalFarmEntity extends BaseElectricEntity<FarmContainer, FarmContainerGUI> {
    private static ArrayList<String> foodItems = new ArrayList<>();

    static {
        AnimalFarmEntity.foodItems.add("minecraft:wheat");
        AnimalFarmEntity.foodItems.add("minecraft:carrot");
        AnimalFarmEntity.foodItems.add("minecraft:potato");
        AnimalFarmEntity.foodItems.add("minecraft:seeds");
    }

    public AnimalFarmEntity() {
        super(1, 500000, 3, 6, 1, FarmContainer.class, FarmContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        if (slot == 0) {
            // test for animal package
            if (stack.getItem() == ItemsRegistry.animalPackage) {
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
    protected boolean performWork() {
        //region process package

        ItemStack packageStack = this.inStackHandler.extractItem(0, 1, true, true);
        if ((packageStack != null) && (packageStack.stackSize > 0)) {
            ItemStack stackCopy = packageStack.copy();

            if ((stackCopy.getItem() instanceof AnimalPackageItem) && !ItemsRegistry.animalPackage.hasAnimal(stackCopy)) {
                EnumFacing facing = BlocksRegistry.animalFarmBlock.getStateFromMeta(this.getBlockMetadata())
                        .getValue(BlocksRegistry.animalFarmBlock.FACING)
                        .getOpposite();
                EnumFacing left = facing.rotateYCCW();
                EnumFacing right = facing.rotateY();
                BlockPos pos1 = this.getPos()
                        .offset(left, ((left == EnumFacing.EAST) || (left == EnumFacing.SOUTH)) ? 4 : 3);
                if ((facing == EnumFacing.SOUTH) || (facing == EnumFacing.EAST)) {
                    pos1 = pos1.offset(facing);
                }
                BlockPos pos2 = this.getPos()
                        .offset(right, ((right == EnumFacing.EAST) || (right == EnumFacing.SOUTH)) ? 4 : 3)
                        .offset(facing, ((facing == EnumFacing.EAST) || (facing == EnumFacing.SOUTH)) ? 8 : 7)
                        .offset(EnumFacing.UP);
                AxisAlignedBB aabb = new AxisAlignedBB(pos1, pos2);

                // find animal
                List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, aabb);
                ItemStack filterStack = this.filtersHandler.getStackInSlot(0, true);
                BaseAnimalFilterItem filter = ((filterStack != null) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                        ? (BaseAnimalFilterItem) filterStack.getItem()
                        : null;
                EntityAnimal animal = null;
                if ((list != null) && (list.size() > 0)) {
                    int i = 0;
                    do {
                        animal = list.get(i++);
                        if ((animal != null)&&(filter != null) && (false == filter.shouldHandle(animal))) {
                            animal = null;
                        }
                    } while ((i < list.size()) && (animal == null));
                }

                if (animal != null) {
                    stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
                    NBTTagCompound animalCompound = new NBTTagCompound();
                    animal.writeEntityToNBT(animalCompound);
                    stackCopy.setTagInfo("animal", animalCompound);
                    stackCopy.setTagInfo("animalClass", new NBTTagString(animal.getClass().getName()));

                    ItemStack finalStack = this.outStackHandler.insertItems(stackCopy, false);
                    int inserted = packageStack.stackSize - ((finalStack == null) ? 0 : finalStack.stackSize);
                    if (inserted > 0) {
                        this.worldObj.removeEntity(animal);
                        this.inStackHandler.extractItem(0, inserted, false, true);
                        return true;
                    }
                }
            }
        }

        //endregion

        //region process food

        //endregion

        return false;
    }
}
