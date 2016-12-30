package mekfarm.machines.wrappers.seeds;

import mekfarm.machines.wrappers.ISeedWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaGenericSeed implements ISeedWrapper {
    private ItemStack seed;

    private IPlantable plantable;

    public VanillaGenericSeed(ItemStack seed) {
        this.plantable = (IPlantable) (this.seed = seed).getItem();
    }

    @Override
    public ItemStack getSeeds() {
        return this.seed;
    }

    @Override
    public boolean canPlantHere(World world, BlockPos pos) {
        return (world.getBlockState(pos.down()).getBlock() == Blocks.FARMLAND)
                && this.plantable.getPlantType(world, pos) == EnumPlantType.Crop;
    }

    @Override
    public IBlockState plant(World world, BlockPos pos) {
        return this.canPlantHere(world, pos)
                ? this.plantable.getPlant(world, pos)
                : null;
    }

    public static boolean isSeed(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack)) {
            Item item = stack.getItem();
            return (item instanceof IPlantable);
        }
        return false;
    }
}
