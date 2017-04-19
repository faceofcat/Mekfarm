package mekfarm.machines.wrappers.seeds;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2017-04-07.
 */
public class VanillaNetherWartSeed extends VanillaGenericSeed {
    public VanillaNetherWartSeed(ItemStack seed) {
        super(seed);
    }

    @Override
    public boolean canPlantHere(World world, BlockPos pos) {
        Block under = world.getBlockState(pos.down()).getBlock();
        return (under == Blocks.SOUL_SAND);
    }

    public static boolean isSeed(ItemStack stack) {
        return (!ItemStackUtil.isEmpty(stack) &&
                (stack.getItem() == Items.NETHER_WART)
        );
    }
}