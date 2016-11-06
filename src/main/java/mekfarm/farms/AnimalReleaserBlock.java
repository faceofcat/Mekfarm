package mekfarm.farms;

import mekfarm.common.BaseOrientedBlock;
import mekfarm.common.BlocksRegistry;
import mekfarm.farms.AnimalFarmEntity;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserBlock extends BaseOrientedBlock<AnimalReleaserEntity> {
    public AnimalReleaserBlock() { super("animal_releaser", AnimalReleaserEntity.class, BlocksRegistry.ANIMAL_RELEASER_GUI_ID); }
}