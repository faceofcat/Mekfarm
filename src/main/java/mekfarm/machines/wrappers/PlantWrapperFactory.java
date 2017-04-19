package mekfarm.machines.wrappers;

import mekfarm.machines.wrappers.plants.*;
import mekfarm.machines.wrappers.seeds.*;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

/**
 * Created by CF on 2016-12-10.
 */
public class PlantWrapperFactory {
    public static ISeedWrapper getSeedWrapper(ItemStack seeds) {
        if (ItemStackUtil.isEmpty(seeds)) {
            return null;
        }
        Item seed = seeds.getItem();

        if ((seed == Items.MELON_SEEDS) || (seed == Items.PUMPKIN_SEEDS)) {
            return new VanillaMelonSeed(seeds.copy());
        }

        if (seed == Items.REEDS) {
            return new VanillaReedsSeed(seeds.copy());
        }

        if (seed == Item.getItemFromBlock(Blocks.CACTUS)) {
            return new VanillaCactusSeed(seeds.copy());
        }

        if (VanillaNetherWartSeed.isSeed(seeds)) {
            return new VanillaNetherWartSeed(seeds.copy());
        }

        if (seed instanceof IPlantable) {
            return new VanillaGenericSeed(seeds.copy());
        }

        return null;
    }

    public static boolean isSeed(ItemStack stack) {
        return VanillaGenericSeed.isSeed(stack)
                || VanillaCactusSeed.isSeed(stack)
                || VanillaMelonSeed.isSeed(stack)
                || VanillaReedsSeed.isSeed(stack)
                || VanillaNetherWartSeed.isSeed(stack);
    }

    public static IPlantWrapper getPlantWrapper(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if ((block == Blocks.MELON_STEM) || (block == Blocks.PUMPKIN_STEM)) {
            return new VanillaMelonPlant(block, state, world, pos);
        }

        if (block == Blocks.REEDS) {
            return new VanillaTallPlant(block, state, world, pos);
        }

        if (block == Blocks.CACTUS) {
            return new VanillaCactusPlant(block, state, world, pos);
        }

        if (block == Blocks.NETHER_WART) {
            return new VanillaNetherWartPlant(state, world, pos);
        }

        if (block instanceof IGrowable) {
            return new VanillaGenericPlant(block, state, world, pos);
        }

        return null;
    }

    public static boolean isFertilizer(ItemStack stack) {
        return ((stack.getItem() == Items.DYE) && (stack.getMetadata() == 15));
    }
}
