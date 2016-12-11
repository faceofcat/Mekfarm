package mekfarm.machines.wrappers.seeds;

import mekfarm.machines.wrappers.ISeedWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaCactusSeed implements ISeedWrapper {
    private ItemStack seeds;

    public VanillaCactusSeed(ItemStack seeds) {
        this.seeds = seeds;
    }

    @Override
    public ItemStack getSeeds() {
        return this.seeds;
    }

    @Override
    public boolean canPlantHere(World world, BlockPos pos) {
        return (world.getBlockState(pos.down()).getBlock() == Blocks.SAND)
                && world.isAirBlock(pos.south())
                && world.isAirBlock(pos.east())
                && world.isAirBlock(pos.north())
                && world.isAirBlock(pos.west());
    }

    @Override
    public IBlockState plant(World world, BlockPos pos) {
        if (this.canPlantHere(world, pos)) {
            return Blocks.CACTUS.getPlant(world, pos);
        }
        return null;
    }

    public static boolean isSeed(ItemStack stack) {
        return (!stack.isEmpty() && (stack.getItem() == Item.getItemFromBlock(Blocks.CACTUS)));
    }
}