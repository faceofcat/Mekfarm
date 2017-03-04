package mekfarm.machines.wrappers.trees;

import mekfarm.machines.wrappers.ITreeSaplingWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2017-02-25.
 */
public class VanillaSapling implements ITreeSaplingWrapper {
    private ItemStack stack;

    public VanillaSapling(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean canPlant(World world, BlockPos pos) {
        if (ItemStackUtil.isEmpty(this.stack)) {
            return false;
        }

        Item item = this.stack.getItem();
        Object rawPlantable = (item instanceof ItemBlock) ? ((ItemBlock) item).getBlock() : item;
        IPlantable plantable = (rawPlantable instanceof IPlantable) ? (IPlantable)rawPlantable : null;
        if (plantable == null) {
            return false;
        }

        IBlockState down = world.getBlockState(pos.down());
        return down.getBlock().canSustainPlant(down, world, pos.down(), EnumFacing.UP, plantable);
    }

    @Override
    public int plant(World world, BlockPos pos) {
        if (this.canPlant(world, pos)) {
            Item item = this.stack.getItem();
            Block block = (item instanceof ItemBlock) ? ((ItemBlock) item).getBlock() : null;

            IBlockState plant = (block == null) ? null : block.getStateFromMeta(this.stack.getItemDamage());
            if (plant != null) {
                world.setBlockState(pos, plant);
                return 1;
            }
        }

        return 0;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }
}
