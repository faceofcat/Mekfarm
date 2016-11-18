package mekfarm.inventories;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * Created by CF on 2016-11-18.
 */
public class SingleFluidTank extends FluidTank {
    public SingleFluidTank(int capacity) {
        super(capacity);
    }

    @Override
    public boolean canDrain() {
        return false;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid){
        return ((fluid != null) && (fluid.containsFluid(new FluidStack(this.getFluidType(), 1))));
    }

    protected Fluid getFluidType() {
        return FluidRegistry.WATER;
    }
}
