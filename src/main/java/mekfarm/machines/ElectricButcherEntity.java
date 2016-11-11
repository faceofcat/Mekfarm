package mekfarm.machines;

import mekfarm.containers.ElectricButcherContainer;
import mekfarm.ui.ElectricButcherContainerGUI;
import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherEntity extends BaseElectricEntity<ElectricButcherContainer, ElectricButcherContainerGUI> {
    public ElectricButcherEntity() {
        super(3, 500000, 1, 6, 1, ElectricButcherContainer.class, ElectricButcherContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        if (slot == 0) {
            // test for weapon
        }
        else if (slot == 1) {
            // test for shears
        }

        return false;
    }

    @Override
    protected float performWork() {
        return 0;
    }
}
