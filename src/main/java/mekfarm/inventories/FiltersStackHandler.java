package mekfarm.inventories;

import mekfarm.capabilities.IFilterHandler;
import mekfarm.items.BaseAnimalFilterItem;
import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2016-11-10.
 */
public class FiltersStackHandler extends FilteredStackHandler implements IFilterHandler {
    public FiltersStackHandler(int size) {
        super(size);
    }

    @Override
    protected boolean acceptsStack(int slot, ItemStack stack, boolean internal) {
        return this.acceptsFilter(slot, stack);
    }

    @Override
    public boolean acceptsFilter(int slot, ItemStack filter) {
        return ((filter != null) && (filter.getItem() instanceof BaseAnimalFilterItem));
    }
}
