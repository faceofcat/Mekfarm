package mekfarm.machines.wrappers.plants;

import com.google.common.collect.Lists;
import mekfarm.machines.wrappers.IPlantWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaTallPlant implements IPlantWrapper {
    private IBlockState state;
    private Block block;
    private World world;
    protected BlockPos pos;

    private static List<Block> PLANT_BLOCKS = Lists.newArrayList(Blocks.CACTUS, Blocks.REEDS);

    public VanillaTallPlant(Block block, IBlockState state, World world, BlockPos pos) {
        this.state = state;
        this.block = block;
        this.world = world;
        this.pos = pos;
    }

    @Override
    public boolean canBeHarvested() {
        return PLANT_BLOCKS.contains(this.world.getBlockState(this.pos.up()).getBlock());
    }

    @Override
    public List<ItemStack> harvest(int fortune) {
        List<ItemStack> loot = Lists.newArrayList();
        this.harvestUp(this.pos.up(), loot, fortune);
        return loot;
    }

    private void harvestUp(BlockPos pos, List<ItemStack> loot, int fortune) {
        IBlockState state = this.world.getBlockState(pos);
        if (!PLANT_BLOCKS.contains(state.getBlock())) {
            return;
        }

        this.harvestUp(pos.up(), loot, fortune);
        loot.addAll(state.getBlock().getDrops(this.world, this.pos, state, fortune));
        this.world.setBlockToAir(pos);
    }

    @Override
    public boolean canBlockNeighbours() {
        return false;
    }

    @Override
    public boolean blocksNeighbour(BlockPos pos) {
        return false;
    }

    @Override
    public boolean canUseFertilizer() {
        return false;
    }

    @Override
    public int useFertilizer(ItemStack fertilizer) {
        return 0;
    }
}
