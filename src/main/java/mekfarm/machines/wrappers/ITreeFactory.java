package mekfarm.machines.wrappers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2017-02-20.
 */
public interface ITreeFactory {
    ITreeLogWrapper getHarvestableLog(World world, BlockPos pos, IBlockState block);
    ITreeLeafWrapper getHarvestableLeaf(World world, BlockPos pos, IBlockState block);
    ITreeSaplingWrapper getPlantableSapling(ItemStack stack);
}
