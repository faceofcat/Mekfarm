package mekfarm.common;

import mekfarm.farms.AnimalFarmBlock;
import mekfarm.farms.AnimalReleaserBlock;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static final int ANIMAL_FARM_GUI_ID = 1;
    public static final int ANIMAL_RELEASER_GUI_ID = 2;

    public static AnimalFarmBlock animalFarmBlock;
    public static AnimalReleaserBlock animalReleaserBlock;

    static void createBlocks() {
        (BlocksRegistry.animalFarmBlock = new AnimalFarmBlock()).register();
        (BlocksRegistry.animalReleaserBlock = new AnimalReleaserBlock()).register();
    }
}
