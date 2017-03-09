package mekfarm.common;

import net.minecraft.entity.passive.EntityAnimal;
import net.ndrei.teslacorelib.tileentities.ElectricTileEntity;

/**
 * Created by CF on 2016-12-06.
 */
public interface IAnimalEntityFilter /*extends IMachineAddon*/ {
    boolean canProcess(ElectricTileEntity machine, int entityIndex, EntityAnimal entity);
}
