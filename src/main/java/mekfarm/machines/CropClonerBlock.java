package mekfarm.machines;

import mekfarm.client.CropClonerSpecialRenderer;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

/**
 * Created by CF on 2016-11-24.
 */
public class CropClonerBlock extends BaseOrientedBlock<CropClonerEntity> {
    public static final PropertyInteger STATE = PropertyInteger.create("state", 0, 1);

    public CropClonerBlock() {
        super("crop_cloner", CropClonerEntity.class);

        super.setDefaultState(super.getDefaultState()
            .withProperty(STATE, 0));
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "rhr", "dcd", "wgw",
                'r', Blocks.REDSTONE_BLOCK,
                'h', Items.DIAMOND_HOE,
                'd', Blocks.DIRT,
                'c', TeslaCoreLib.machineCase,
                'w', Blocks.PLANKS,
                'g', TeslaCoreLib.gearIron);
    }

    public static void setState(IBlockState newState, World worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, newState);
        if (tileEntity != null)
        {
            tileEntity.validate();
            worldIn.setTileEntity(pos, tileEntity);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, STATE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        int state = meta & 1;
        EnumFacing enumfacing = EnumFacing.getFront((meta >> 1));
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState()
                .withProperty(FACING, enumfacing)
                .withProperty(STATE, state);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getIndex();
        meta = meta << 1;
        meta += state.getValue(STATE);
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected TileEntitySpecialRenderer<CropClonerEntity> getSpecialRenderer() {
        return new CropClonerSpecialRenderer();
    }

    // TODO: find out what this should be replaced with
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    // TODO: find out what this should be replaced with
    @Override
    @SuppressWarnings("deprecation")
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    // TODO: find out what this should be replaced with
    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    // TODO: find out what this should be replaced with
    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
