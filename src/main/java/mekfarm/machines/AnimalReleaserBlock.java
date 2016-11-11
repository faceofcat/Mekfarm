package mekfarm.machines;

import mekfarm.common.BlocksRegistry;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaserBlock extends BaseOrientedBlock<AnimalReleaserEntity> {
    public AnimalReleaserBlock() { super("animal_releaser", AnimalReleaserEntity.class, BlocksRegistry.ANIMAL_RELEASER_GUI_ID); }
}