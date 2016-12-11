package mekfarm.machines.wrappers.plants;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaCactusPlant extends VanillaTallPlant {
    public VanillaCactusPlant(Block block, IBlockState state, World world, BlockPos pos) {
        super(block, state, world, pos);
    }

    @Override
    public boolean canBlockNeighbours() {
        return true;
    }

    @Override
    public boolean blocksNeighbour(BlockPos pos) {
        return pos.equals(super.pos.offset(EnumFacing.EAST))
                || pos.equals(super.pos.offset(EnumFacing.NORTH))
                || pos.equals(super.pos.offset(EnumFacing.SOUTH))
                || pos.equals(super.pos.offset(EnumFacing.WEST));
    }
}
