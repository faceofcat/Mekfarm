package mekfarm.machines;

import mekfarm.common.BlocksRegistry;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherBlock extends BaseOrientedBlock<ElectricButcherEntity> {
    public ElectricButcherBlock() { super("electric_butcher", ElectricButcherEntity.class, BlocksRegistry.ELECTRIC_BUTCHER_GUI_ID); }
}