package mekfarm.machines.wrappers.plants;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.FakeMekPlayer;
import mekfarm.machines.wrappers.IPlantWrapper;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.List;

/**
 * Created by CF on 2017-04-07.
 */
public class VanillaNetherWartPlant implements IPlantWrapper {
    private IBlockState state;
    private World world;
    private BlockPos pos;

    public VanillaNetherWartPlant(IBlockState state, World world, BlockPos pos) {
        this.state = state;
        this.world = world;
        this.pos = pos;
    }

    @Override
    public boolean canBeHarvested() {
        return this.state.getValue(BlockNetherWart.AGE) == 3;
    }

    @Override
    public List<ItemStack> harvest(int fortune) {
        // return Blocks.NETHER_WART.getDrops(this.world, this.pos, this.state, fortune);
        FakeMekPlayer player = MekfarmMod.getFakePlayer(this.world);
        this.state.getBlock().harvestBlock(this.world, player, this.pos, this.state, null, ItemStackUtil.getEmptyStack());
        this.world.setBlockState(this.pos, this.state.getBlock().getDefaultState());
        this.world.destroyBlock(this.pos,false); // <-- to force replanting

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
        return false;
    }

    @Override
    public int useFertilizer(ItemStack fertilizer) {
        return 0;
    }
}