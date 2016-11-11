package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import mekfarm.containers.FarmContainer;
import mekfarm.ui.AnimalReleaserContainerGUI;
import mekfarm.items.AnimalPackageItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserEntity extends BaseElectricEntity<FarmContainer, AnimalReleaserContainerGUI> {
    public AnimalReleaserEntity() {
        super(2, 500000, 3, 3, 0, FarmContainer.class, AnimalReleaserContainerGUI.class);
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
            if ((stack != null) && (stack.stackSize > 0)) {
                break;
            }
        }
        if ((stack != null) && (stack.stackSize > 0)) {
            ItemStack stackCopy = stack.copy();
            if ((stackCopy.getItem() instanceof AnimalPackageItem) && stackCopy.hasTagCompound()) {
                NBTTagCompound compound = stackCopy.getTagCompound();
                compound.removeTag("hasAnimal");
                if (compound.hasKey("animal") && compound.hasKey("animalClass")) {
                    NBTTagCompound animal = compound.getCompoundTag("animal");
                    String animalClass = compound.getString("animalClass");
                    try {
                        Class cea = Class.forName(animalClass);
                        Object thing = cea.getConstructor(World.class).newInstance(this.worldObj);
                        if (thing instanceof EntityAnimal) {
                            EntityAnimal ea = (EntityAnimal)thing;
                            ea.readEntityFromNBT(animal);

                            EnumFacing facing = BlocksRegistry.animalReleaserBlock.getStateFromMeta(this.getBlockMetadata())
                                    .getValue(BaseOrientedBlock.FACING)
                                    .getOpposite();
                            EnumFacing left = facing.rotateYCCW();
                            EnumFacing right = facing.rotateY();
                            BlockPos pos = this.getPos()
                                    .offset(left, 3)
                                    .offset(right, Math.round((float)Math.random() * 7))
                                    .offset(facing, 1 + Math.round((float)Math.random() * 7));
                            ea.setPosition(pos.getX(), pos.getY(), pos.getZ());

                            stackCopy.setTagCompound(null);
                            ItemStack finalStack = this.outStackHandler.insertItems(stackCopy, false);
                            int inserted = stack.stackSize - ((finalStack == null) ? 0 : finalStack.stackSize);
                            if (inserted > 0) {
                                this.inStackHandler.extractItem(stackIndex, inserted, false, true);
                                this.worldObj.spawnEntityInWorld(ea);
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
