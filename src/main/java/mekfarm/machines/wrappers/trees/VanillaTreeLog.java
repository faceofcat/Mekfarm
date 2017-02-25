package mekfarm.machines.wrappers.trees;

import mekfarm.machines.wrappers.ITreeLogWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CF on 2017-02-25.
 */
public class VanillaTreeLog extends VanillaTreeBlock implements ITreeLogWrapper {
    VanillaTreeLog(World world, BlockPos pos) {
        super(world, pos);
    }
}
