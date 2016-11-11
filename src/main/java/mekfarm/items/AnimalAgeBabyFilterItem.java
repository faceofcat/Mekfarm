package mekfarm.items;

import net.minecraft.entity.passive.EntityAnimal;

/**
 * Created by CF on 2016-11-10.
 */
public class AnimalAgeBabyFilterItem extends BaseAnimalFilterItem {
    public AnimalAgeBabyFilterItem() {
        super("animal_age_filter_baby");
    }

    @Override
    public boolean shouldHandle(EntityAnimal animal) {
        return ((animal != null) && animal.isChild());
    }
}
