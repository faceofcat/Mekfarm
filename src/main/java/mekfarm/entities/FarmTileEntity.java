package mekfarm.entities;

import mekfarm.items.AnimalPackage;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmTileEntity extends BaseElectricEntity {
    public FarmTileEntity() {
        super(1, 500000, 3, 3);
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
            if (stackCopy.getItem() instanceof AnimalPackage) {
                stackCopy.setTagInfo("hasAnimal", new NBTTagInt(1));
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
