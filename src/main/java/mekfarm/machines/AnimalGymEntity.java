package mekfarm.machines;

import net.ndrei.teslacorelib.tileentities.ElectricGenerator;

/**
 * Created by CF on 2017-01-15.
 */
public class AnimalGymEntity extends ElectricGenerator {
    public AnimalGymEntity() {
        super(AnimalGymEntity.class.hashCode());
    }

    @Override
    protected long consumeFuel() {
        return 0;
    }
}
