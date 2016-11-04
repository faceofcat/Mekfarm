package mekfarm.common;

import mekfarm.blocks.AnimalFarm;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static final int ANIMAL_FARM_GUI_ID = 1;
    public static final int ANIMAL_RELEASER_GUI_ID = 2;

    public static AnimalFarm farmBlock;

    public static final void createBlocks() {
        (BlocksRegistry.farmBlock = new AnimalFarm()).register();
    }
}
