package mekfarm.inventories;

import mekfarm.MekfarmMod;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Created by CF on 2016-10-31.
 */
public class EnergyStorage implements ITeslaConsumer, ITeslaHolder, IEnergyStorage, INBTSerializable<NBTTagCompound> {
    private int storedPower = 0;
    private int maxPower = 1000000;

    public EnergyStorage(int maxStoredEnergy) {
        this.maxPower = maxStoredEnergy;
    }

    @Override
    public long getStoredPower() {
        return this.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return this.getMaxEnergyStored();
    }

    @Override
    public long givePower(long power, boolean simulated) {
        return this.receiveEnergy((int)power, simulated);
    }

    @Override
    public boolean canExtract() {
        // MekfarmMod.logger.info("can extract: false");
        return false;
    }

    @Override
    public boolean canReceive() {
        // MekfarmMod.logger.info("can receive: true");
        return true;
    }

    @Override
    public int getEnergyStored() {
        // MekfarmMod.logger.info("stored energy: " + this.storedPower);
        return this.storedPower;
    }

    @Override
    public int getMaxEnergyStored() {
        // MekfarmMod.logger.info("max energy: " + this.maxPower);
        return this.maxPower;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        // MekfarmMod.logger.info("receive: " + maxReceive + " : " + simulate);
        if (false == this.canReceive()) {
            return 0;
        }
        int canReceive = Math.min(maxReceive, this.maxPower - this.storedPower);
        if (canReceive > 0) {
            if (false == simulate) {
                this.storedPower += canReceive;
                // MekfarmMod.logger.info("Received power: " + canReceive + " / " + this.storedPower);
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
                // MekfarmMod.logger.info("Extracted power: " + canExtract);
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
