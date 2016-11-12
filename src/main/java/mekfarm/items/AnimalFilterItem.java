package mekfarm.items;

import mekfarm.common.ItemsRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2016-11-10.
 */
public class AnimalFilterItem extends BaseItem {
    public AnimalFilterItem() {
        super("animal_filter");
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "x x", " y ", "x x",
                'x', Items.WHEAT,
                'y', ItemsRegistry.baseFilterItem);
    }
}
