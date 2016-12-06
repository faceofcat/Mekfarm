package mekfarm.inventories;

import mekfarm.capabilities.IFilterHandler;
import mekfarm.common.IMachineAddon;
import mekfarm.items.BaseAnimalFilterItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-11-10.
 */
public class FiltersStackHandler extends FilteredStackHandler implements IFilterHandler {
    private TileEntity machine;

    public FiltersStackHandler(TileEntity machine, int size) {
        super(size);
        this.machine = machine;
    }

    @Override
    protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
        return this.acceptsFilter(slot, stack);
    }

    @Override
    public boolean acceptsFilter(int slot, ItemStack filter) {
        if ((this.machine != null) && (filter != null) && !filter.isEmpty()) {
            Item item = filter.getItem();
            if (item instanceof IMachineAddon) {
                return ((IMachineAddon) item).canBeAddedTo(this.machine);
            }
        }

        return false;
    }
}
