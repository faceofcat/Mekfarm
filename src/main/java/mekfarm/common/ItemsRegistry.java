package mekfarm.common;

import mekfarm.items.*;

/**
 * Created by CF on 2016-10-26.
 */
public final class ItemsRegistry {
    public static AnimalPackageItem animalPackage;
    public static AnimalFilterItem animalFilter;
    public static AnimalAgeAdultFilterItem animalAgeAdultFilter;
    public static AnimalAgeBabyFilterItem animalAgeBabyFilter;
    public static LiquidXPCollectorItem liquidXPCollectorItem;

    public static final void createItems() {
        (ItemsRegistry.animalPackage = new AnimalPackageItem()).register();
        (ItemsRegistry.animalFilter = new AnimalFilterItem()).register();
        (ItemsRegistry.animalAgeAdultFilter = new AnimalAgeAdultFilterItem()).register();
        (ItemsRegistry.animalAgeBabyFilter = new AnimalAgeBabyFilterItem()).register();
        (ItemsRegistry.liquidXPCollectorItem = new LiquidXPCollectorItem()).register();
    }
}
