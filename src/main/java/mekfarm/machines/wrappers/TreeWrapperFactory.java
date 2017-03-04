package mekfarm.machines.wrappers;

import com.google.common.collect.Lists;
import mekfarm.machines.wrappers.trees.VanillaTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.List;

/**
 * Created by CF on 2017-02-20.
 */
public class TreeWrapperFactory {
    private static List<ITreeFactory> treeWrappers;

    static {
        TreeWrapperFactory.treeWrappers = Lists.newArrayList();
        TreeWrapperFactory.treeWrappers.add(new VanillaTree());
    }

    public static boolean isHarvestable(World world, BlockPos pos, IBlockState block) {
        if (block == null) {
            block = world.getBlockState(pos);
        }
        return TreeWrapperFactory.isHarvestableLog(world, pos, block)
                || TreeWrapperFactory.isHarvestableLeaf(world, pos, block);
    }

    public static boolean isHarvestableLog(World world, BlockPos pos, IBlockState block) {
        if (block == null) {
            block = world.getBlockState(pos);
        }
        for(ITreeFactory tree : TreeWrapperFactory.treeWrappers) {
            if (tree.getHarvestableLog(world, pos, block) != null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHarvestableLeaf(World world, BlockPos pos, IBlockState block) {
        if (block == null) {
            block = world.getBlockState(pos);
        }
        for(ITreeFactory tree : TreeWrapperFactory.treeWrappers) {
            if (tree.getHarvestableLeaf(world, pos, block) != null) {
                return true;
            }
        }
        return false;
    }

    public static ITreeBlockWrapper getBlockWrapper(World world, BlockPos pos, IBlockState block) {
        if (block == null) {
            block = world.getBlockState(pos);
        }
        for(ITreeFactory tree : TreeWrapperFactory.treeWrappers) {
            ITreeBlockWrapper wrapper = tree.getHarvestableLog(world, pos, block);
            if (wrapper == null) {
                wrapper = tree.getHarvestableLeaf(world, pos, block);
            }

            if (wrapper != null) {
                return wrapper;
            }
        }
        return null;
    }

    public static ITreeSaplingWrapper getSaplingWrapper(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack)) {
            for (ITreeFactory tree : TreeWrapperFactory.treeWrappers) {
                ITreeSaplingWrapper wrapper = tree.getPlantableSapling(stack);
                if (wrapper != null) {
                    return wrapper;
                }
            }
        }
        return null;
    }

    public static List<ITreeSaplingWrapper> getSaplingWrappers(List<ItemStack> stacks) {
        List<ITreeSaplingWrapper> wrappers = Lists.newArrayList();

        if (stacks != null) {
            for(ItemStack stack : stacks) {
                ITreeSaplingWrapper wrapper = TreeWrapperFactory.getSaplingWrapper(stack);
                if (wrapper != null) {
                    wrappers.add(wrapper);
                }
            }
        }

        return wrappers;
    }
}
