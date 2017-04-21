package mekfarm.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2016-10-26.
 */
public class AnimalFarmBlock extends BaseOrientedBlock<AnimalFarmEntity> {
    public AnimalFarmBlock() {
        super("animal_farm", AnimalFarmEntity.class);
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xyz", "wcw", "wgw",
                'x', "cropWheat", // Items.WHEAT,
                'y', "cropCarrot", // Items.CARROT,
                'z', "cropWheat", // Items.WHEAT,
                'c', TeslaCoreLib.machineCase,
                'w', "plankWood", // Blocks.PLANKS,
                'g', "gearStone" // TeslaCoreLib.gearStone
        );
    }
}
