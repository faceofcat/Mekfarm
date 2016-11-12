package mekfarm.common;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by CF on 2016-11-12.
 */
public class BlockPosUtils {
    public static BlockCube getCube(BlockPos entityPos, EnumFacing facing, int radius, int height) {
        EnumFacing left = facing.rotateYCCW();
        EnumFacing right = facing.rotateY();
        BlockPos pos1 = entityPos
                .offset(left, radius)
                .offset(facing, 1);
        BlockPos pos2 = entityPos
                .offset(right, radius)
                .offset(facing, radius * 2 + 1)
                .offset(EnumFacing.UP, height - 1);

        return new BlockCube(pos1, pos2);
    }
}
