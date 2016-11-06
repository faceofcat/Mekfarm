package mekfarm.farms;

import mekfarm.MekfarmMod;
import mekfarm.common.BaseElectricEntity;
import mekfarm.common.ItemsRegistry;
import mekfarm.containers.FarmContainer;
import mekfarm.containers.FarmContainerGUI;
import mekfarm.items.AnimalPackage;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

import java.util.ArrayList;

/**
 * Created by CF on 2016-10-28.
 */
public class AnimalFarmEntity extends BaseElectricEntity<FarmContainer, FarmContainerGUI> {
    private static ArrayList<String> foodItems = new ArrayList<>();

    static {
        AnimalFarmEntity.foodItems.add("minecraft:wheat");
        AnimalFarmEntity.foodItems.add("minecraft:carrot");
        AnimalFarmEntity.foodItems.add("minecraft:potato");
    }

    public AnimalFarmEntity() {
        super(1, 500000, 3, 3, FarmContainer.class, FarmContainerGUI.class);
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

            MekfarmMod.logger.info("Farmed: " + stackCopy.getItem());
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
