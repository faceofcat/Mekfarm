package mekfarm.common;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-12-06.
 */
public interface ICropFarmUpgrade extends IMachineAddon {
    boolean actsAsHoeFor(TileEntity machine);
    boolean actsAsFertilizerFor(TileEntity machine);
}
