package mekfarm.items;

import net.minecraft.entity.passive.EntityAnimal;

/**
 * Created by CF on 2016-11-10.
 */
public class AnimalAgeAdultFilterItem extends BaseAnimalFilterItem {
    public AnimalAgeAdultFilterItem() {
        super("animal_age_filter_adult");
    }

    @Override
    public boolean shouldHandle(EntityAnimal animal) {
        return ((animal != null) && (animal.isChild() == false));
    }
}
