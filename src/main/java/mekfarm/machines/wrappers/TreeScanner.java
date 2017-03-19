package mekfarm.machines.wrappers;

import com.google.common.collect.Lists;
import mekfarm.common.BlockCube;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CF on 2017-02-20.
 */
public class TreeScanner /*implements INBTSerializable<NBTTagCompound>*/ {
    private List<BlockPos> scanned = null;
    private List<BlockPos> toScan = null;

    public TreeScanner() {
        this.scanned = Lists.newArrayList();
    }

    public float scan(World world, BlockCube base, float perBlockValue, float maxValue) {
        float result = 0;

        if (this.scanned == null) {
            this.scanned = Lists.newArrayList();
        }
        if (this.toScan == null) {
            this.toScan = Lists.newArrayList();
        }

        // always scan first level
        for(BlockPos pos : base) {
           if (!this.scanned.contains(pos) && !this.toScan.contains(pos)
                   && TreeWrapperFactory.isHarvestable(world, pos, null)) {
               this.toScan.add(0, pos);

               result += perBlockValue;
               if (result >= maxValue) {
                   return result;
               }
           }
        }

        // scan nearby blocks
        while(toScan.size() > 0) {
            BlockPos pos = this.toScan.get(0);

            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos nb = pos.offset(facing);
                if (!this.scanned.contains(nb) && !this.toScan.contains(nb)
                        && TreeWrapperFactory.isHarvestable(world, nb, null)) {
                    this.toScan.add(nb);

                    result += perBlockValue;
                    if (result >= maxValue) {
                        return result;
                    }
                }
            }

            this.toScan.remove(0);
            this.scanned.add(pos);
        }

        return result;
    }

    public int blockCount() {
        return (this.scanned == null) ? 0 : this.scanned.size();
    }

    public BlockPos popScannedPos() {
        if ((this.scanned == null) || (this.scanned.size() == 0)) {
            return null;
        }

        int index = this.scanned.size() - 1;
        BlockPos pos = this.scanned.get(index);
        this.scanned.remove(index);
        return pos;
    }

//    @Override
//    public NBTTagCompound serializeNBT() {
//        NBTTagCompound compound = new NBTTagCompound();
//
//        return compound;
//    }
//
//    @Override
//    public void deserializeNBT(NBTTagCompound nbt) {
//
//    }
}
