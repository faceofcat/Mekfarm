package mekfarm.machines.wrappers.animals;

import mekfarm.machines.wrappers.IAnimalWrapper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by CF on 2016-12-10.
 */
public class VanillaHorse extends VanillaGenericAnimal {
    private EntityHorse horse;

    public VanillaHorse(EntityHorse horse) {
        super(horse);
        this.horse = horse;
    }

    @Override
    public boolean canMateWith(IAnimalWrapper wrapper) {
        return this.breedable() && wrapper.breedable() && (wrapper instanceof VanillaHorse);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return (stack.getItem() == Items.GOLDEN_CARROT) || (stack.getItem() == Items.APPLE);
    }

    @Override
    protected int getFoodNeededForMating(ItemStack stack) {
        return (stack.getItem() == Items.GOLDEN_CARROT)
                ? 1
                : 6; // assume apple
    }

    public static void populateFoodItems(List<Item> food) {
        food.add(Items.GOLDEN_CARROT);
        food.add(Items.APPLE);
    }
}

