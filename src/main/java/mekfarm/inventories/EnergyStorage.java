package mekfarm.inventories;

import mekanism.api.energy.IStrictEnergyAcceptor;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

/**
 * Created by CF on 2016-10-31.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "tesla"),
        @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaHolder", modid = "tesla"),
        @Optional.Interface(iface = "mekanism.api.energy.IStrictEnergyAcceptor", modid = "Mekanism")
})
public class EnergyStorage implements ITeslaConsumer, ITeslaHolder, IStrictEnergyAcceptor, IEnergyStorage, INBTSerializable<NBTTagCompound> {
    private int storedPower = 0;
    private int maxPower = 1000000;

    public EnergyStorage(int maxStoredEnergy) {
        this.maxPower = maxStoredEnergy;
    }

    //region ITeslaHolder

    @Override
    public long getStoredPower() {
        return this.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return this.getMaxEnergyStored();
    }

    //endregion
    //region ITeslaConsumer

    @Override
    public long givePower(long power, boolean simulated) {
        return this.receiveEnergy((int)power, simulated);
    }

    //endregion
    //region IStrictEnergyAcceptor

    @Override
    public double getEnergy() {
        return this.getEnergyStored();
    }

    @Override
    public void setEnergy(double energy) {
        // TODO: ??
    }

    @Override
    public double getMaxEnergy() {
        return this.getMaxEnergyStored();
    }

    @Override
    public double transferEnergyToAcceptor(EnumFacing side, double amount) {
        int tesla = Math.round((float)amount * .4f);
        tesla = this.receiveEnergy(tesla, false);
        return tesla / .4;
    }

    @Override
    public boolean canReceiveEnergy(EnumFacing side) {
        return this.canReceive();
    }

    //endregion

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public int getEnergyStored() {
        return this.storedPower;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.maxPower;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (false == this.canReceive()) {
            return 0;
        }
        int canReceive = Math.min(maxReceive, this.maxPower - this.storedPower);
        if (canReceive > 0) {
            if (false == simulate) {
                this.storedPower += canReceive;
                this.onChanged();
            }
            return canReceive;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.extractEnergy(maxExtract, simulate, false);
    }

    public int extractEnergy(int maxExtract, boolean simulate, boolean force) {
        if (!force && (false == this.canExtract())) {
            return 0;
        }
        int canExtract = Math.min(maxExtract, this.storedPower);
        if (canExtract > 0) {
            if (false == simulate) {
                this.storedPower -= canExtract;
                this.onChanged();
            }
            return canExtract;
        }
        return 0;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("energy", this.storedPower);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if ((nbt != null) && nbt.hasKey("energy")) {
            this.storedPower = nbt.getInteger("energy");
        }
    }

    public void onChanged() { }
}
