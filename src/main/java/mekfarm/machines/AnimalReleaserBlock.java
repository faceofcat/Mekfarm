package mekfarm.machines;

import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserBlock extends BaseOrientedBlock<AnimalReleaserEntity> {
    public AnimalReleaserBlock() { super("animal_releaser", AnimalReleaserEntity.class, BlocksRegistry.ANIMAL_RELEASER_GUI_ID); }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "wxw", "wcw", "www",
                'x', Blocks.DISPENSER,
                'c', ItemsRegistry.machineCase,
                'w', Blocks.PLANKS);
    }
}