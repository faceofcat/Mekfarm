package mekfarm.machines.wrappers.trees;

import com.google.common.collect.Lists;
import mekfarm.machines.wrappers.ITreeLeafWrapper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CF on 2017-02-25.
 */
public class VanillaTreeLeaf extends VanillaTreeBlock implements ITreeLeafWrapper {
    VanillaTreeLeaf(World world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public List<ItemStack> shearBlock() {
        List<ItemStack> stacks = Lists.newArrayList();

        Block block = this.world.getBlockState(this.pos).getBlock();
        if ((block == Blocks.LEAVES) || (block == Blocks.LEAVES2)) {
            stacks.add(new ItemStack(block));
        }
        this.world.destroyBlock(this.pos, false);

        return stacks;
    }
}
