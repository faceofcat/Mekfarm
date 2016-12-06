package mekfarm.common;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-12-06.
 */
public interface IAnimalFarmUpgrade extends IMachineAddon {
    boolean actsAsShearsFor(TileEntity machine);
}
