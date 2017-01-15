package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherBlock extends BaseOrientedBlock<ElectricButcherEntity> {
    public ElectricButcherBlock() { super("electric_butcher", ElectricButcherEntity.class); }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "wxw", "wcw", "www",
                'x', Items.DIAMOND_SWORD,
                'c', TeslaCoreLib.machineCase,
                'w', Blocks.PLANKS);
    }
}