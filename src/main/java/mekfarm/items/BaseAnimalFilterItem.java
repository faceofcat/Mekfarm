package mekfarm.items;

import mekfarm.MekfarmMod;
import mekfarm.common.IAnimalAgeFilterAcceptor;
import mekfarm.common.IAnimalEntityFilter;
import net.ndrei.teslacorelib.items.BaseAddon;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

/**
 * Created by CF on 2016-11-10.
 */
public abstract class BaseAnimalFilterItem extends BaseAddon implements IAnimalEntityFilter {
    public BaseAnimalFilterItem(String registryName) {
        super(MekfarmMod.MODID, MekfarmMod.creativeTab, registryName);
    }

    @Override
    public boolean canBeAddedTo(SidedTileEntity machine) {
        return (machine != null) && (machine instanceof IAnimalAgeFilterAcceptor)
                && ((IAnimalAgeFilterAcceptor)machine).acceptsFilter(this);
    }
}
