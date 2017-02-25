package mekfarm.machines.wrappers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2017-02-25.
 */
public interface ITreeSaplingWrapper {
    boolean canPlant(World world, BlockPos pos);
    int plant(World world, BlockPos pos);

    ItemStack getStack();
}
