package mekfarm.machines.wrappers.trees;

import mekfarm.machines.wrappers.ITreeFactory;
import mekfarm.machines.wrappers.ITreeLeafWrapper;
import mekfarm.machines.wrappers.ITreeLogWrapper;
import mekfarm.machines.wrappers.ITreeSaplingWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2017-02-20.
 */
public class VanillaTree implements ITreeFactory {
    @Override
    public ITreeLogWrapper getHarvestableLog(World world, BlockPos pos, IBlockState block) {
        return ((block != null) && ((block.getBlock() == Blocks.LOG) || (block.getBlock() == Blocks.LOG2)))
                ? new VanillaTreeLog(world, pos)
                : null;
    }

    @Override
    public ITreeLeafWrapper getHarvestableLeaf(World world, BlockPos pos, IBlockState block) {
        return ((block != null) && ((block.getBlock() == Blocks.LEAVES) || (block.getBlock() == Blocks.LEAVES2)))
                ? new VanillaTreeLeaf(world, pos)
                : null;
    }

    @Override
    public ITreeSaplingWrapper getPlantableSapling(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() == Item.getItemFromBlock(Blocks.SAPLING))) {
            return new VanillaSapling(stack);
        }
        return null;
    }
}
