package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.ItemsRegistry;
import mekfarm.items.AnimalPackageItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserEntity extends ElectricMekfarmMachine {
    public AnimalReleaserEntity() {
        super(AnimalReleaserEntity.class.hashCode());
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (stack == null)
            return true;

        // test for animal package
        if (stack.getItem().getRegistryName().equals(ItemsRegistry.animalPackage.getRegistryName())) {
            return ItemsRegistry.animalPackage.hasAnimal(stack);
        }
        return false;
    }

    @Override
    protected void createAddonsInventory() {
    }

    @Override
    protected float performWork() {
        ItemStack stack = ItemStackUtil.getEmptyStack();
        int stackIndex = 0;
        for (; stackIndex < this.inStackHandler.getSlots(); stackIndex++) {
            stack = this.inStackHandler.extractItem(stackIndex, 1, true);
            if (!ItemStackUtil.isEmpty(stack)) {
                break;
            }
        }
        if (!ItemStackUtil.isEmpty(stack)) {
            ItemStack stackCopy = stack.copy();
            if ((stackCopy.getItem() instanceof AnimalPackageItem) && stackCopy.hasTagCompound()) {
                NBTTagCompound compound = stackCopy.getTagCompound();
                compound.removeTag("hasAnimal");
                if (compound.hasKey("animal") && compound.hasKey("animalClass")) {
                    NBTTagCompound animal = compound.getCompoundTag("animal");
                    String animalClass = compound.getString("animalClass");
                    try {
                        Class cea = Class.forName(animalClass);
                        Object thing = cea.getConstructor(World.class).newInstance(this.getWorld());
                        if (thing instanceof EntityAnimal) {
                            EntityAnimal ea = (EntityAnimal)thing;
                            ea.readEntityFromNBT(animal);

                            EnumFacing facing = super.getFacing().getOpposite();
                            BlockCube cube = BlockPosUtils.getCube(this.getPos().offset(facing, 1), facing, 2, 1);
                            BlockPos pos = cube.getRandomInside(this.getWorld().rand);
                            ea.setPosition(pos.getX(), pos.getY(), pos.getZ());

                            stackCopy.setTagCompound(null);
                            ItemStack finalStack = ItemHandlerHelper.insertItem(this.outStackHandler, stackCopy, false);
                            int inserted = ItemStackUtil.getSize(stack) - ItemStackUtil.getSize(finalStack);
                            if (inserted > 0) {
                                this.inStackHandler.extractItem(stackIndex, inserted, false);
                                this.getWorld().spawnEntity(ea);
                                return 1.0f;
                            }
                        }
                    }
                    catch(Throwable e) {
                        MekfarmMod.logger.warn("Error creating animal '" + animalClass + "'.", e);
                    }
                }
            }
        }
        return 0.0f;
    }
}
