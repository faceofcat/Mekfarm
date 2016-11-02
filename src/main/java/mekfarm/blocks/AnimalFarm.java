package mekfarm.blocks;

import mekfarm.common.BlocksRegistry;
import mekfarm.entities.FarmTileEntity;

/**
 * Created by CF on 2016-10-26.
 */
public class AnimalFarm extends BaseOrientedBlock<FarmTileEntity> {
    public AnimalFarm() {
        super("animal_farm", FarmTileEntity.class, BlocksRegistry.ANIMAL_FARM_GUI_ID);
    }
}
