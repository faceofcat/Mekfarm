package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserBlock extends BaseOrientedBlock<AnimalReleaserEntity> {
    public AnimalReleaserBlock() { super("animal_releaser", AnimalReleaserEntity.class); }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "wxw", "wcw", "wgw",
                'x', Blocks.DISPENSER,
                'c', TeslaCoreLib.machineCase,
                'w', Blocks.PLANKS,
                'g', TeslaCoreLib.gearStone);
    }
}