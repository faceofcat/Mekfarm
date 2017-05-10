package mekfarm.machines.wrappers;

import mekfarm.machines.wrappers.animals.MooFluidCow;
import mekfarm.machines.wrappers.animals.VanillaGenericAnimal;
import mekfarm.machines.wrappers.animals.VanillaHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;

import java.util.List;

/**
 * Created by CF on 2016-12-10.
 */
public class AnimalWrapperFactory {
    /**
     * @param entity
     * the entity to be wrapped
     * @return
     * return a nicely wrapped entity ready to be exploited in many many ways
     */
    public static IAnimalWrapper getAnimalWrapper(EntityAnimal entity) {
        IAnimalWrapper wrapper = null;

        if (entity instanceof EntityHorse) {
            wrapper = new VanillaHorse((EntityHorse) entity);
        }

        if ((entity instanceof EntityCow) && entity.getClass().getName().equals("com.robrit.moofluids.common.entity.EntityFluidCow")) {
            wrapper = new MooFluidCow((EntityCow)entity);
        }

        if (wrapper == null) {
            wrapper = new VanillaGenericAnimal(entity);
        }
        return wrapper;
    }

    public static void populateFoodItems(List<Item> food) {
        VanillaGenericAnimal.populateFoodItems(food);
        VanillaHorse.populateFoodItems(food);
    }
}
