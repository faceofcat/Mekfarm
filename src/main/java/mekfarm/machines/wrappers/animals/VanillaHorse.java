package mekfarm.machines.wrappers.animals;

import mekfarm.machines.wrappers.IAnimalWrapper;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2016-12-10.
 */
public class VanillaHorse extends VanillaGenericAnimal {
    private AbstractHorse horse;

    public VanillaHorse(AbstractHorse horse) {
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
}

