package mekfarm.common;

import mekfarm.items.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CF on 2016-10-26.
 */
public final class ItemsRegistry {
    public static AnimalPackageItem animalPackage;
    public static AnimalFilterItem animalFilter;
    public static AnimalAgeAdultFilterItem animalAgeAdultFilter;
    public static AnimalAgeBabyFilterItem animalAgeBabyFilter;
    public static BaseFilterItem baseFilterItem;

    public static final void createItems() {
        GameRegistry.register(ItemsRegistry.animalPackage = new AnimalPackageItem());
        GameRegistry.register(ItemsRegistry.animalFilter = new AnimalFilterItem());
        GameRegistry.register(ItemsRegistry.animalAgeAdultFilter = new AnimalAgeAdultFilterItem());
        GameRegistry.register(ItemsRegistry.animalAgeBabyFilter = new AnimalAgeBabyFilterItem());
        GameRegistry.register(ItemsRegistry.baseFilterItem = new BaseFilterItem());
    }
}
