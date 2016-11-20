package mekfarm.common;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by CF on 2016-11-12.
 */
public class BlockCube implements Iterable<BlockPos> {
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
                this.northWest.getX() + Math.round(rnd.nextFloat() * x),
                this.northWest.getY() + Math.round(rnd.nextFloat() * y),
                this.northWest.getZ() + Math.round(rnd.nextFloat() * z)
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

    @Override
    public Iterator<BlockPos> iterator() {
        return new BlockPosIterator(this);
    }

    private class BlockPosIterator implements Iterator<BlockPos> {
        private int position = 0;
        private int minX;
        private int minY;
        private int minZ;
        private int maxX;
        private int maxY;
        private int maxZ;

        private int xSize;
        private int ySize;
        private int zSize;

        public BlockPosIterator(BlockCube cube) {
            this.minX = cube.northWest.getX();
            this.minY = cube.northWest.getY();
            this.minZ = cube.northWest.getZ();
            this.maxX = cube.southEast.getX();
            this.maxY = cube.southEast.getY();
            this.maxZ = cube.southEast.getZ();

            this.xSize = this.maxX - this.minX + 1;
            this.ySize = this.maxY - this.minY + 1;
            this.zSize = this.maxZ - this.minZ + 1;
        }

        @Override
        public boolean hasNext() {
            return position < (this.xSize * this.ySize * this.zSize);
        }

        @Override
        public BlockPos next() {
            int plane = position % (this.xSize * this.zSize);
            int y = position / (this.xSize * this.zSize);
            int x = plane % this.zSize;
            int z = plane / this.zSize;
            position++;
            return new BlockPos(this.minX + x, this.minY + y, this.minZ + z);
        }
    }
}
