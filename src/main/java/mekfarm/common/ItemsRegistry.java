package mekfarm.common;

import mekfarm.items.FarmItem;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CF on 2016-10-26.
 */
public final class ItemsRegistry {
    public static FarmItem farmItem;

    public static final void createItems() {
        GameRegistry.register(ItemsRegistry.farmItem = new FarmItem());
    }
}
