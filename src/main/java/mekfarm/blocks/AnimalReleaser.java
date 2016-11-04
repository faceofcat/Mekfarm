package mekfarm.blocks;

import mekfarm.common.BlocksRegistry;
import mekfarm.entities.FarmTileEntity;

/**
 * Created by CF on 2016-11-04.
 */
public class AnimalReleaser extends BaseOrientedBlock<FarmTileEntity> {
    public AnimalReleaser() {
        super("animal_releaser", FarmTileEntity.class, BlocksRegistry.ANIMAL_RELEASER_GUI_ID);
    }
}