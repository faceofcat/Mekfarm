package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2016-12-09.
 */
public class AnimalGymBlock extends BaseOrientedBlock<AnimalGymEntity> {
    public AnimalGymBlock() {
        super("animal_gym", AnimalGymEntity.class);
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xgx", "wcw", "wgw",
                'x', Blocks.IRON_BARS,
                'c', TeslaCoreLib.machineCase,
                'w', "plankWood", // Blocks.PLANKS,
                'g', "gearIron" // TeslaCoreLib.gearIron
        );
    }
}
