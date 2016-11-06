package mekfarm.farms;

import mekfarm.common.BaseElectricEntity;
import mekfarm.common.BaseOrientedBlock;
import mekfarm.common.BlocksRegistry;
import mekfarm.containers.FarmContainer;
import mekfarm.containers.FarmContainerGUI;
import mekfarm.items.AnimalPackage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserEntity extends BaseElectricEntity<FarmContainer, FarmContainerGUI> {
    public AnimalReleaserEntity() {
        super(1, 500000, 3, 3, FarmContainer.class, FarmContainerGUI.class);
    }

    @Override
    protected boolean performWork() {
        ItemStack stack = null;
        int stackIndex = 0;
        for (; stackIndex < this.inStackHandler.getSlots(); stackIndex++) {
            stack = this.inStackHandler.extractItem(stackIndex, 1, true);
            if ((stack != null) && (stack.stackSize > 0)) {
                break;
            }
        }
        if ((stack != null) && (stack.stackSize > 0)) {
            int original = stack.stackSize;
            ItemStack stackCopy = stack.copy();
            if ((stackCopy.getItem() instanceof AnimalPackage) && stackCopy.hasTagCompound()) {
                stackCopy.getTagCompound().removeTag("hasAnimal");
                if (stackCopy.getTagCompound().getSize() == 0) {
                    stackCopy.setTagCompound(null);
                }
            }

            ItemStack finalStack = this.outStackHandler.insertItems(stackCopy, false);
            int inserted = original - ((finalStack == null) ? 0 : finalStack.stackSize);
            if (inserted > 0) {
                this.inStackHandler.extractItem(stackIndex, inserted, false);
                return true;
            }
        }
        return false;
    }
}
