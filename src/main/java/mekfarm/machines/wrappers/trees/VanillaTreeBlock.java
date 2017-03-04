package mekfarm.machines.wrappers.trees;

import mekfarm.machines.wrappers.ITreeBlockWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CF on 2017-02-25.
 */
public abstract class VanillaTreeBlock implements ITreeBlockWrapper {
    protected final World world;
    protected final BlockPos pos;

    protected VanillaTreeBlock(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    @Override
    public List<ItemStack> breakBlock(int fortune) {
        IBlockState state = this.world.getBlockState(this.pos);
        List<ItemStack> stacks = state.getBlock().getDrops(world, pos, state, fortune);

        this.world.destroyBlock(this.pos, false);

        return stacks;
    }
}
