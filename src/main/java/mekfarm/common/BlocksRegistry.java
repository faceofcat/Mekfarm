package mekfarm.common;

import mekfarm.machines.*;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static AnimalFarmBlock animalFarmBlock;
    public static AnimalReleaserBlock animalReleaserBlock;
    public static ElectricButcherBlock electricButcherBlock;
    public static CropFarmBlock cropFarmBlock;
    public static CropClonerBlock cropClonerBlock;
    public static AnimalGymBlock animalGymBlock;
    public static TreeFarmBlock treeFarmBlock;

    static void createBlocks() {
        (BlocksRegistry.animalFarmBlock = new AnimalFarmBlock()).register();
        (BlocksRegistry.animalReleaserBlock = new AnimalReleaserBlock()).register();
        (BlocksRegistry.electricButcherBlock = new ElectricButcherBlock()).register();
        (BlocksRegistry.cropFarmBlock = new CropFarmBlock()).register();
        (BlocksRegistry.cropClonerBlock = new CropClonerBlock()).register();
        (BlocksRegistry.animalGymBlock = new AnimalGymBlock()).register();
        (BlocksRegistry.treeFarmBlock = new TreeFarmBlock()).register();
    }
}
