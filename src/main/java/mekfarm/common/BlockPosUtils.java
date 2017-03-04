package mekfarm.common;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by CF on 2016-11-12.
 */
public class BlockPosUtils {
    public static BlockCube getCube(BlockPos entityPos, EnumFacing facing, int radius, int height) {
        BlockPos pos1, pos2;

        if (facing != null) {
            if (facing == EnumFacing.UP) {
                pos1 = entityPos
                        .offset(EnumFacing.EAST, radius)
                        .offset(EnumFacing.SOUTH, radius)
                        .up(1);
                pos2 = entityPos
                        .offset(EnumFacing.WEST, radius)
                        .offset(EnumFacing.NORTH, radius)
                        .up(height);
            }
            else if (facing == EnumFacing.DOWN) {
                pos1 = entityPos
                        .offset(EnumFacing.EAST, radius)
                        .offset(EnumFacing.SOUTH, radius)
                        .down(1);
                pos2 = entityPos
                        .offset(EnumFacing.WEST, radius)
                        .offset(EnumFacing.NORTH, radius)
                        .down(height);
            }
            else {
                // assume horizontal facing
                EnumFacing left = facing.rotateYCCW();
                EnumFacing right = facing.rotateY();
                pos1 = entityPos
                        .offset(left, radius)
                        .offset(facing, 1);
                pos2 = entityPos
                        .offset(right, radius)
                        .offset(facing, radius * 2 + 1);
            }
        }
        else {
            pos1 = new BlockPos(entityPos.getX() - radius, entityPos.getY(), entityPos.getZ() - radius);
            pos2 = new BlockPos(entityPos.getX() + radius, entityPos.getY(), entityPos.getZ() + radius);
        }
        pos2 = pos2.offset(EnumFacing.UP, height - 1);

        return new BlockCube(pos1, pos2);
    }
}
