package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2017-02-28.
 */
public class SewerBlock extends BaseOrientedBlock<SewerEntity> {
    public SewerBlock() {
        super("sewer", SewerEntity.class);
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xxx", "wcw", "wgw",
                'x', Blocks.IRON_BARS,
                'c', TeslaCoreLib.machineCase,
                'w', Blocks.PLANKS,
                'g', TeslaCoreLib.gearStone);
    }
}
