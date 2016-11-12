package mekfarm.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2016-11-12.
 */
public class MachineCaseItem extends BaseItem {
    public MachineCaseItem() {
        super("machine_case");
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xyx", "yzy", "xyx",
                'x', Items.IRON_INGOT,
                'y', Blocks.PLANKS,
                'z', Blocks.REDSTONE_BLOCK);
    }
}
