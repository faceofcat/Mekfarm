package mekfarm.items;

import net.minecraft.entity.passive.EntityAnimal;

/**
 * Created by CF on 2016-11-10.
 */
public abstract class BaseAnimalFilterItem extends BaseItem {
    public BaseAnimalFilterItem(String registryName) {
        super(registryName);
    }

    // TODO: maybe add an interface for this
    public abstract boolean shouldHandle(EntityAnimal animal);
}
