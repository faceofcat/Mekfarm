package mekfarm.items;

import mekfarm.common.IAnimalEntityFilter;
import mekfarm.machines.AnimalFarmEntity;
import mekfarm.machines.ElectricButcherEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-11-10.
 */
public abstract class BaseAnimalFilterItem extends BaseItem implements IAnimalEntityFilter {
    public BaseAnimalFilterItem(String registryName) {
        super(registryName);
    }

    @Override
    public boolean canBeAddedTo(TileEntity machine) {
        if (machine == null) {
            return false;
        }

        return ((machine instanceof AnimalFarmEntity) || (machine instanceof ElectricButcherEntity));
    }

    @Override
    public float getPowerMultiplier() {
        return 1.2f;
    }
}
