package mekfarm.machines.wrappers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by CF on 2016-12-10.
 */
public interface IPlantWrapper {
    boolean canBeHarvested();
    List<ItemStack> harvest(int fortune);

    boolean canBlockNeighbours();
    boolean blocksNeighbour(BlockPos pos);

    boolean canUseFertilizer();
    int useFertilizer(ItemStack fertilizer);
}
