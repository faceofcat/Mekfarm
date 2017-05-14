package mekfarm.machines.wrappers.plants;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.FakeMekPlayer;
import mekfarm.machines.wrappers.IPlantWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.List;

/**
 * Created by CF on 2016-12-10.
 */
public class VanillaGenericPlant implements IPlantWrapper {
    protected IBlockState state;
    protected Block block;
    protected World world;
    protected BlockPos pos;

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
    public List<ItemStack> harvest(int fortune) {
        FakeMekPlayer player = MekfarmMod.getFakePlayer(this.world);
        this.state.getBlock().harvestBlock(this.world, player, this.pos, this.state, null, ItemStackUtil.getEmptyStack());
        this.world.setBlockState(this.pos, this.state.getBlock().getDefaultState());
        this.world.destroyBlock(this.pos, false); // <-- to force replanting

        return this.harvestDrops();
    }

    protected List<ItemStack> harvestDrops() {
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

    @Override
    public boolean canUseFertilizer() {
        return !this.canBeHarvested();
    }

    @Override
    public int useFertilizer(ItemStack fertilizer) {
        return VanillaGenericPlant.useFertilizer(this.world, this.pos, fertilizer);
    }

    public static int useFertilizer(World world, BlockPos pos, ItemStack fertilizer) {
        FakeMekPlayer player = MekfarmMod.getFakePlayer(world);
        if (player != null) {
            player.setItemInUse(fertilizer.copy());
            EnumActionResult result = player.getHeldItem(EnumHand.MAIN_HAND).onItemUse(player, world, pos, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f);
            player.setItemInUse(ItemStackUtil.getEmptyStack());
            return 1;
        }
        return 0;
    }
}
