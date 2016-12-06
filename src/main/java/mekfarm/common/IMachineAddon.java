package mekfarm.common;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-12-06.
 */
public interface IMachineAddon {
    boolean canBeAddedTo(TileEntity machine);
    float getPowerMultiplier();
}
