package mekfarm.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by CF on 2016-11-10.
 */
public interface IFilterHandler extends IItemHandler {
    boolean acceptsFilter(int slot, ItemStack filter);
}
