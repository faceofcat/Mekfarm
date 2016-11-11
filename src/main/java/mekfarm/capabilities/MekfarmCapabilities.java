package mekfarm.capabilities;

import mekfarm.inventories.FiltersStackHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by CF on 2016-11-10.
 * mekfarm.capabilities.MekfarmCapabilities <-- that sounds like the worst namespace / class name combination ever
 */
public class MekfarmCapabilities {
    @CapabilityInject(IFilterHandler.class)
    public static Capability<IFilterHandler> CAPABILITY_FILTERS_HANDLER = null;

    @CapabilityInject(IMachineInfo.class)
    public static Capability<IMachineInfo> CAPABILITY_MACHINE_INFO = null;

    public static class CapabilityFilterHandlerConsumer<T extends IFilterHandler> implements Capability.IStorage<IFilterHandler> {
        @Override
        public NBTBase writeNBT (Capability<IFilterHandler> capability, IFilterHandler instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT (Capability<IFilterHandler> capability, IFilterHandler instance, EnumFacing side, NBTBase nbt) {
        }
    }

    public static class CapabilityMachineInfoConsumer<T extends IMachineInfo> implements Capability.IStorage<IMachineInfo> {
        @Override
        public NBTBase writeNBT (Capability<IMachineInfo> capability, IMachineInfo instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT (Capability<IMachineInfo> capability, IMachineInfo instance, EnumFacing side, NBTBase nbt) {
        }
    }

    public static class DefaultMachineInfo implements IMachineInfo {
        @Override
        public String getUnlocalizedMachineName() {
            return "mekfarm.machine";
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IFilterHandler.class, new MekfarmCapabilities.CapabilityFilterHandlerConsumer<>(), FiltersStackHandler.class);
        CapabilityManager.INSTANCE.register(IMachineInfo.class, new MekfarmCapabilities.CapabilityMachineInfoConsumer<>(), DefaultMachineInfo.class);
    }
}