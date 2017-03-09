package mekfarm.common;

import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2017-03-08.
 */
public interface ILiquidXPCollector {
    boolean hasXPCollector();

    void onLiquidXPAddonAdded(ItemStack stack);
    void onLiquidXPAddonRemoved(ItemStack stack);
}
