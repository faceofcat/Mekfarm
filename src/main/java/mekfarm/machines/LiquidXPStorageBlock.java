package mekfarm.machines;

import mekfarm.client.LiquidXPStorageSpecialRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    @Override
    @SideOnly(Side.CLIENT)
    protected TileEntitySpecialRenderer<LiquidXPStorageEntity> getSpecialRenderer() {
        return new LiquidXPStorageSpecialRenderer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return (face == EnumFacing.DOWN);
    }
}