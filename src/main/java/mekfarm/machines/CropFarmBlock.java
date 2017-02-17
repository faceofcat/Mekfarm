package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2016-11-15.
 */
public class CropFarmBlock extends BaseOrientedBlock<CropFarmEntity> {
    public CropFarmBlock() {
        super("crop_farm", CropFarmEntity.class);
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xyz", "acb", "wgw",
                'x', Items.WHEAT_SEEDS,
                'y', Items.DIAMOND_HOE,
                'z', Items.WHEAT_SEEDS,
                'a', Items.CARROT,
                'b', Items.POTATO,
                'c', TeslaCoreLib.machineCase,
                'w', Blocks.PLANKS,
                'g', TeslaCoreLib.gearStone);
    }
}