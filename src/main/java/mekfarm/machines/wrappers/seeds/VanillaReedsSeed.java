package mekfarm.machines.wrappers.seeds;

import mekfarm.machines.wrappers.ISeedWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaReedsSeed implements ISeedWrapper {
    private ItemStack seeds;

    public VanillaReedsSeed(ItemStack seeds) {
        this.seeds = seeds;
    }

    @Override
    public ItemStack getSeeds() {
        return this.seeds;
    }

    @Override
    public boolean canPlantHere(World world, BlockPos pos) {
        Block under = world.getBlockState(pos.down()).getBlock();
        return (
                (under == Blocks.SAND) || (under == Blocks.DIRT)
        ) && (
                (world.getBlockState(pos.north().down()).getBlock() == Blocks.WATER)
                        || (world.getBlockState(pos.east().down()).getBlock() == Blocks.WATER)
                        || (world.getBlockState(pos.south().down()).getBlock() == Blocks.WATER)
                        || (world.getBlockState(pos.west().down()).getBlock() == Blocks.WATER)
        );
    }

    @Override
    public IBlockState plant(World world, BlockPos pos) {
        if (this.canPlantHere(world, pos)) {
            return Blocks.REEDS.getPlant(world, pos);
        }
        return null;
    }

    public static boolean isSeed(ItemStack stack) {
        return (!ItemStackUtil.isEmpty(stack) && (stack.getItem() == Items.REEDS));
    }}
