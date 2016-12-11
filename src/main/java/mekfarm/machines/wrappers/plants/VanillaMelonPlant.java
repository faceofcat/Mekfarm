package mekfarm.machines.wrappers.plants;

import com.google.common.collect.Lists;
import mekfarm.machines.wrappers.IPlantWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaMelonPlant implements IPlantWrapper {
    private IBlockState state;
    private Block block;
    private World world;
    private BlockPos pos;

    private static List<Block> MELON_BLOCKS = Lists.newArrayList(Blocks.MELON_BLOCK, Blocks.PUMPKIN);

    public VanillaMelonPlant(Block block, IBlockState state, World world, BlockPos pos) {
        this.state = state;
        this.block = block;
        this.world = world;
        this.pos = pos;
    }

    @Override
    public boolean canBeHarvested() {
        IBlockState north = this.world.getBlockState(this.pos.north());
        if (MELON_BLOCKS.contains(north.getBlock())) {
            return true;
        }
        IBlockState east = this.world.getBlockState(this.pos.east());
        if (MELON_BLOCKS.contains(east.getBlock())) {
            return true;
        }
        IBlockState south = this.world.getBlockState(this.pos.south());
        if (MELON_BLOCKS.contains(south.getBlock())) {
            return true;
        }
        IBlockState west = this.world.getBlockState(this.pos.west());
        if (MELON_BLOCKS.contains(west.getBlock())) {
            return true;
        }
        return false;
    }

    @Override
    public List<ItemStack> harvest(int fortune) {
        List<ItemStack> loot = Lists.newArrayList();
        IBlockState north = this.world.getBlockState(this.pos.north());
        if (MELON_BLOCKS.contains(north.getBlock())) {
            loot.add(new ItemStack(Item.getItemFromBlock(north.getBlock())));
            this.world.setBlockState(this.pos.north(), Blocks.AIR.getDefaultState());
        }
        IBlockState east = this.world.getBlockState(this.pos.east());
        if (MELON_BLOCKS.contains(east.getBlock())) {
            loot.add(new ItemStack(Item.getItemFromBlock(east.getBlock())));
            this.world.setBlockState(this.pos.east(), Blocks.AIR.getDefaultState());
        }
        IBlockState south = this.world.getBlockState(this.pos.south());
        if (MELON_BLOCKS.contains(south.getBlock())) {
            loot.add(new ItemStack(Item.getItemFromBlock(south.getBlock())));
            this.world.setBlockState(this.pos.south(), Blocks.AIR.getDefaultState());
        }
        IBlockState west = this.world.getBlockState(this.pos.west());
        if (MELON_BLOCKS.contains(west.getBlock())) {
            loot.add(new ItemStack(Item.getItemFromBlock(west.getBlock())));
            this.world.setBlockState(this.pos.west(), Blocks.AIR.getDefaultState());
        }
        return loot;
    }

    @Override
    public boolean canBlockNeighbours() {
        return true;
    }

    @Override
    public boolean blocksNeighbour(BlockPos pos) {
        return pos.equals(this.pos.offset(EnumFacing.EAST))
                || pos.equals(this.pos.offset(EnumFacing.NORTH))
                || pos.equals(this.pos.offset(EnumFacing.SOUTH))
                || pos.equals(this.pos.offset(EnumFacing.WEST));
    }

    @Override
    public boolean canUseFertilizer() {
        if (this.block instanceof IGrowable) {
            return ((IGrowable)this.block).canGrow(this.world, this.pos, this.state, false);
        }
        return false;
    }

    @Override
    public int useFertilizer(ItemStack fertilizer) {
        return VanillaGenericPlant.useFertilizer(this.world, this.pos, fertilizer);
    }
}
