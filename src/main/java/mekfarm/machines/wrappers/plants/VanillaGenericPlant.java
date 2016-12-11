package mekfarm.machines.wrappers.plants;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.FakeMekPlayer;
import mekfarm.machines.wrappers.IPlantWrapper;
import mekfarm.machines.wrappers.animals.VanillaGenericAnimal;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CF on 2016-12-10.
 */
public class VanillaGenericPlant implements IPlantWrapper {
    private IBlockState state;
    private Block block;
    private World world;
    private BlockPos pos;

    private IGrowable growable;

    public VanillaGenericPlant(Block block, IBlockState state, World world, BlockPos pos) {
        this.state = state;
        this.growable = (IGrowable)(this.block = block);
        this.world = world;
        this.pos = pos;
    }

    @Override
    public boolean canBeHarvested() {
        return !this.growable.canGrow(this.world, this.pos, this.state, false);
    }

    @Override
    public List<ItemStack> harvest() {
        FakeMekPlayer player = MekfarmMod.getFakePlayer(this.world);
        state.getBlock().harvestBlock(this.world, player, pos, state, null, ItemStack.EMPTY);
        this.world.setBlockState(pos, state.getBlock().getDefaultState());
        this.world.destroyBlock(pos,true); // <-- to force replanting

        List<ItemStack> items = Lists.newArrayList();
        AxisAlignedBB aabb = BlockPosUtils
                .getCube(this.pos, null, 1, 1)
                .getBoundingBox();
        List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, aabb);
        for (EntityItem ei : entities) {
            items.add(ei.getEntityItem());
            this.world.removeEntity(ei);
        }
        return items;
    }

    @Override
    public boolean canBlockNeighbours() {
        return false;
    }

    @Override
    public boolean blocksNeighbour(BlockPos pos) {
        return false;
    }
}
