package mekfarm.common;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CF on 2016-12-06.
 */
public interface IAnimalEntityFilter extends IMachineAddon {
    boolean canProcess(TileEntity machine, int entityIndex, EntityAnimal entity);
}
