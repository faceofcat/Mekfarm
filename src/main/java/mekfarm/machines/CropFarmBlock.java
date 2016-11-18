package mekfarm.machines;

import mekfarm.common.BlocksRegistry;

/**
 * Created by CF on 2016-11-15.
 */
public class CropFarmBlock extends BaseOrientedBlock<CropFarmEntity> {
    public CropFarmBlock() {
        super("crop_farm", CropFarmEntity.class, BlocksRegistry.CROP_FARM_GUI_ID);
    }
}