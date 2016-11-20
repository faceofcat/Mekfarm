package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import mekfarm.containers.AnimalReleaserContainer;
import mekfarm.items.AnimalPackageItem;
import mekfarm.ui.FarmContainerGUI;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserEntity extends BaseElectricEntity<AnimalReleaserContainer, FarmContainerGUI> {
    public AnimalReleaserEntity() {
        super(2, 500000, 3, 3, 0, AnimalReleaserContainer.class, FarmContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        // test for animal package
        if (stack.getItem().getRegistryName().equals(ItemsRegistry.animalPackage.getRegistryName())) {
            return ItemsRegistry.animalPackage.hasAnimal(stack);
        }
        return false;
    }

    @Override
    protected float performWork() {
        ItemStack stack = null;
        int stackIndex = 0;
        for (; stackIndex < this.inStackHandler.getSlots(); stackIndex++) {
            stack = this.inStackHandler.extractItem(stackIndex, 1, true, true);
            if ((stack != null) && (stack.getCount() > 0)) {
                break;
            }
        }
        if ((stack != null) && (stack.getCount() > 0)) {
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

                            EnumFacing facing = BlocksRegistry.animalReleaserBlock.getStateFromMeta(this.getBlockMetadata())
                                    .getValue(BlocksRegistry.animalReleaserBlock.FACING)
                                    .getOpposite();
                            BlockCube cube = BlockPosUtils.getCube(this.getPos().offset(facing, 1), facing, 2, 1);
                            BlockPos pos = cube.getRandomInside(this.getWorld().rand);
                            ea.setPosition(pos.getX(), pos.getY(), pos.getZ());

                            stackCopy.setTagCompound(null);
                            ItemStack finalStack = this.outStackHandler.distributeItems(stackCopy, false);
                            int inserted = stack.getCount() - ((finalStack == null) ? 0 : finalStack.getCount());
                            if (inserted > 0) {
                                this.inStackHandler.extractItem(stackIndex, inserted, false, true);
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
