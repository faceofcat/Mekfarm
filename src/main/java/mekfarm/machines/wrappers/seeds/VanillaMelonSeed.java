package mekfarm.machines.wrappers.seeds;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2016-12-11.
 */
public class VanillaMelonSeed extends VanillaGenericSeed {
    public VanillaMelonSeed(ItemStack seed) {
        super(seed);
    }

    @Override
    public boolean canPlantHere(World world, BlockPos pos) {
        return (world.isAirBlock(pos.north())
                || world.isAirBlock(pos.east())
                || world.isAirBlock(pos.south())
                || world.isAirBlock(pos.west())
        ) && super.canPlantHere(world, pos);
    }

    public static boolean isSeed(ItemStack stack) {
        return (!ItemStackUtil.isEmpty(stack) &&
                ((stack.getItem() == Items.MELON_SEEDS) || (stack.getItem() == Items.PUMPKIN_SEEDS))
        );
    }
}
