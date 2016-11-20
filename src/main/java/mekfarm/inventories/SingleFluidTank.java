package mekfarm.inventories;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

/**
 * Created by CF on 2016-11-18.
 */
public class SingleFluidTank extends FluidTank {
    private boolean canDrainOverride = false;

    public SingleFluidTank(int capacity) {
        super(capacity);
    }

    @Override
    public boolean canDrain() {
        return this.canDrainOverride;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid){
        return ((fluid != null) && (fluid.containsFluid(new FluidStack(this.getFluidType(), 1))));
    }

    protected Fluid getFluidType() {
        return FluidRegistry.WATER;
    }

    @Nullable
    public FluidStack drain(FluidStack resource, boolean doDrain, boolean internal) {
        try {
            this.canDrainOverride = internal;
            return super.drain(resource, doDrain);
        }
        finally {
            this.canDrainOverride = false;
        }
    }

    @Nullable
    public FluidStack drain(int maxDrain, boolean doDrain, boolean internal) {
        try {
            this.canDrainOverride = internal;
            return super.drain(maxDrain, doDrain);
        }
        finally {
            this.canDrainOverride = false;
        }
    }
}
