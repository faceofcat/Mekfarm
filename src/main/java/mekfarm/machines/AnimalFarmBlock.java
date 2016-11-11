package mekfarm.machines;

import mekfarm.common.BlocksRegistry;

/**
 * Created by CF on 2016-10-26.
 */
public class AnimalFarmBlock extends BaseOrientedBlock<AnimalFarmEntity> {
    public AnimalFarmBlock() {
        super("animal_farm", AnimalFarmEntity.class, BlocksRegistry.ANIMAL_FARM_GUI_ID);
    }
}
