package mekfarm.machines.wrappers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2016-12-10.
 */
public interface ISeedWrapper {
    ItemStack getSeeds();

    boolean canPlantHere(World world, BlockPos pos);
    IBlockState plant(World world, BlockPos pos);
}
