package mekfarm.machines;

import mekfarm.MekfarmMod;
import net.minecraft.block.material.Material;
import net.ndrei.teslacorelib.blocks.OrientedBlock;

/**
 * Created by CF on 2016-11-02.
 */
public abstract class BaseOrientedBlock<T extends ElectricMekfarmMachine> extends OrientedBlock { // implements ITileEntityProvider, ITeslaWrenchHandler {
    protected BaseOrientedBlock(String blockId, Class<T> teClass) {
        this(blockId, teClass, Material.ROCK);
    }

    protected BaseOrientedBlock(String blockId, Class<T> teClass, Material material) {
        super(MekfarmMod.MODID, MekfarmMod.creativeTab, blockId, teClass, material);
    }

//    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
//                                    EnumFacing side, float hitX, float hitY, float hitZ) {
//        if (super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) {
//            return true;
//        }
//
//        // Only execute on the server
//        if (this.guiId > 0) {
//            if (!world.isRemote) {
//                player.openGui(MekfarmMod.instance, this.guiId, world, pos.getX(), pos.getY(), pos.getZ());
//            }
//            return true;
//        }
//
//        return false;
//    }

//    @Override
//    public EnumActionResult onWrenchUse(TeslaWrench wrench,
//                                        EntityPlayer player, World world, BlockPos pos, EnumHand hand,
//                                        EnumFacing facing, float hitX, float hitY, float hitZ) {
//        IBlockState state = world.getBlockState(pos);
//        if (state.getBlock() == this) {
//            TileEntity tileEntity = world.getTileEntity(pos);
//            state = state.withProperty(OrientedBlock.FACING, state.getValue(OrientedBlock.FACING).rotateY());
//            world.setBlockState(pos, state);
//            if (tileEntity != null)
//            {
//                tileEntity.validate();
//                world.setTileEntity(pos, tileEntity);
//            }
//            return EnumActionResult.SUCCESS;
//        }
//
//        return EnumActionResult.PASS;
//    }
}
