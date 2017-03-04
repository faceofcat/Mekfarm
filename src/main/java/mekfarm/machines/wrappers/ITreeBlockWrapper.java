package mekfarm.machines.wrappers;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by CF on 2017-02-25.
 */
public interface ITreeBlockWrapper {
    List<ItemStack> breakBlock(int fortune);
}
