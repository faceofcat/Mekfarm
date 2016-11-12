package mekfarm.common;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by CF on 2016-11-12.
 */
public class BlockCube {
    private BlockPos southEast;
    private BlockPos northWest;

    BlockCube(BlockPos pos1, BlockPos pos2) {
//        the x-axis indicates the player's distance east (positive) or west (negative) of the origin point
//        the z-axis indicates the player's distance south (positive) or north (negative) of the origin point
//        the y-axis indicates how high or low (from 0 to 255, with 64 being sea level)
        this.southEast = new BlockPos(
                Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ())
        );
        this.northWest = new BlockPos(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ())
        );
    }

    public BlockPos getSouthEast() {
        return this.southEast;
    }

    public BlockPos getNorthWest() {
        return this.northWest;
    }

    public BlockPos getRandomInside(@Nullable Random rnd) {
        int x = this.southEast.getX() - this.northWest.getX();
        int y = this.southEast.getY() - this.northWest.getY();
        int z = this.southEast.getZ() - this.northWest.getZ();

        if (rnd == null) {
            rnd = new Random();
        }
        return new BlockPos(
                this.southEast.getX() + Math.round(rnd.nextFloat() * x),
                this.southEast.getY() + Math.round(rnd.nextFloat() * y),
                this.southEast.getZ() + Math.round(rnd.nextFloat() * z)
        );
    }

    public AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(
                this.northWest.getX(),
                this.northWest.getY(),
                this.northWest.getZ(),
                this.southEast.getX() + 1,
                this.southEast.getY() + 1,
                this.southEast.getZ() + 1);
    }
}
