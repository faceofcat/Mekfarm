package mekfarm.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2016-11-10.
 */
public class BaseFilterItem extends BaseItem {
    public BaseFilterItem() {
        super("base_filter");
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xyx", "xxx", "xyx",
                'x', Items.PAPER,
                'y', Items.REDSTONE);
    }
}
