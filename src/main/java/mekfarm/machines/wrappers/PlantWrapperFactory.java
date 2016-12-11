package mekfarm.machines.wrappers;

import mekfarm.machines.wrappers.plants.VanillaCactusPlant;
import mekfarm.machines.wrappers.plants.VanillaGenericPlant;
import mekfarm.machines.wrappers.plants.VanillaMelonPlant;
import mekfarm.machines.wrappers.plants.VanillaTallPlant;
import mekfarm.machines.wrappers.seeds.VanillaCactusSeed;
import mekfarm.machines.wrappers.seeds.VanillaGenericSeed;
import mekfarm.machines.wrappers.seeds.VanillaMelonSeed;
import mekfarm.machines.wrappers.seeds.VanillaReedsSeed;
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

/**
 * Created by CF on 2016-12-10.
 */
public class PlantWrapperFactory {
    public static ISeedWrapper getSeedWrapper(ItemStack seeds) {
        if ((seeds == null) || seeds.isEmpty()) {
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

        if (seed instanceof IPlantable) {
            return new VanillaGenericSeed(seeds.copy());
        }

        return null;
    }

    public static boolean isSeed(ItemStack stack) {
        return VanillaGenericSeed.isSeed(stack)
                || VanillaCactusSeed.isSeed(stack)
                || VanillaMelonSeed.isSeed(stack)
                || VanillaReedsSeed.isSeed(stack);
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
        if (block instanceof IGrowable) {
            return new VanillaGenericPlant(block, state, world, pos);
        }

        return null;
    }
}
