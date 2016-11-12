package mekfarm.items;

import mekfarm.common.ItemsRegistry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

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

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "   ", " y ", "xxx",
                'x', Items.REDSTONE,
                'y', ItemsRegistry.animalFilter);
    }
}
