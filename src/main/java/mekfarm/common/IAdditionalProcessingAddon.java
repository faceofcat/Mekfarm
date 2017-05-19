package mekfarm.common;

import mekfarm.machines.ElectricMekfarmMachine;

/**
 * Created by CF on 2017-05-19.
 */
public interface IAdditionalProcessingAddon {
    float processAddon(ElectricMekfarmMachine machine, float availableProcessing);
}
