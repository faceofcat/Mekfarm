package mekfarm.machines.wrappers.animals;

import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by CF on 2016-12-10.
 */
public class VanillaLlama extends VanillaGenericAnimal {
    private EntityLlama llama;

    public VanillaLlama(EntityLlama llama) {
        super(llama);
        this.llama = llama;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return (stack.getItem() == Items.WHEAT) || (stack.getItem() == Item.getItemFromBlock(Blocks.HAY_BLOCK));
    }

    @Override
    protected int getFoodNeededForMating(ItemStack stack) {
        return (stack.getItem() == Item.getItemFromBlock(Blocks.HAY_BLOCK))
                ? 1
                : 9; // assume wheat
    }
}

