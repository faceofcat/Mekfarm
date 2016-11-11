package mekfarm.common;

import mekfarm.machines.AnimalFarmBlock;
import mekfarm.machines.AnimalReleaserBlock;
import mekfarm.machines.ElectricButcherBlock;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static final int ANIMAL_FARM_GUI_ID = 1;
    public static final int ANIMAL_RELEASER_GUI_ID = 2;
    public static final int ELECTRIC_BUTCHER_GUI_ID = 3;

    public static AnimalFarmBlock animalFarmBlock;
    public static AnimalReleaserBlock animalReleaserBlock;
    public static ElectricButcherBlock electricButcherBlock;

    static void createBlocks() {
        (BlocksRegistry.animalFarmBlock = new AnimalFarmBlock()).register();
        (BlocksRegistry.animalReleaserBlock = new AnimalReleaserBlock()).register();
        (BlocksRegistry.electricButcherBlock = new ElectricButcherBlock()).register();
    }
}
