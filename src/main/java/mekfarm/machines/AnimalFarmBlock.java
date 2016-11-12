package mekfarm.machines;

import mekfarm.common.BlocksRegistry;
import mekfarm.common.ItemsRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2016-10-26.
 */
public class AnimalFarmBlock extends BaseOrientedBlock<AnimalFarmEntity> {
    public AnimalFarmBlock() {
        super("animal_farm", AnimalFarmEntity.class, BlocksRegistry.ANIMAL_FARM_GUI_ID);
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xyz", "wcw", "www",
                'x', Items.WHEAT,
                'y', Items.CARROT,
                'z', Items.WHEAT,
                'c', ItemsRegistry.machineCase,
                'w', Blocks.PLANKS);
    }
}
