package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2017-03-09.
 */
public class LiquidXPStorageBlock extends BaseOrientedBlock<LiquidXPStorageEntity> {
    public LiquidXPStorageBlock() {
        super("liquidxp_storage", LiquidXPStorageEntity.class);
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                " s ", "wcw", "wgw",
                's', Items.EXPERIENCE_BOTTLE,
                'c', TeslaCoreLib.machineCase,
                'w', Blocks.PLANKS,
                'g', TeslaCoreLib.gearIron);
    }
}