package mekfarm.capabilities;

/**
 * Created by CF on 2016-11-10.
 * mekfarm.capabilities.MekfarmCapabilities <-- that sounds like the worst namespace / class name combination ever
 */
public class MekfarmCapabilities {
//    @CapabilityInject(IAnimalAgeFilterAcceptor.class)
//    public static Capability<IAnimalAgeFilterAcceptor> CAPABILITY_ANIMAL_AGE_FILTER = null;

//    @CapabilityInject(IMachineInfo.class)
//    public static Capability<IMachineInfo> CAPABILITY_MACHINE_INFO = null;

//    public static class CapabilityAnimalAgeFilterConsumer<T extends IAnimalAgeFilterAcceptor> implements Capability.IStorage<IAnimalAgeFilterAcceptor> {
//        @Override
//        public NBTBase writeNBT (Capability<IAnimalAgeFilterAcceptor> capability, IAnimalAgeFilterAcceptor instance, EnumFacing side) {
//            return null;
//        }
//
//        @Override
//        public void readNBT (Capability<IAnimalAgeFilterAcceptor> capability, IAnimalAgeFilterAcceptor instance, EnumFacing side, NBTBase nbt) {
//        }
//    }

//    public static class CapabilityMachineInfoConsumer<T extends IMachineInfo> implements Capability.IStorage<IMachineInfo> {
//        @Override
//        public NBTBase writeNBT (Capability<IMachineInfo> capability, IMachineInfo instance, EnumFacing side) {
//            return null;
//        }
//
//        @Override
//        public void readNBT (Capability<IMachineInfo> capability, IMachineInfo instance, EnumFacing side, NBTBase nbt) {
//        }
//    }

//    public static class DefaultAnimalFilterAcceptor implements IAnimalAgeFilterAcceptor {
//        @Override
//        public boolean acceptsFilter(BaseAnimalFilterItem item) {
//            return false;
//        }
//    }

    public static void register() {
//        CapabilityManager.INSTANCE.register(IAnimalAgeFilterAcceptor.class, new MekfarmCapabilities.CapabilityAnimalAgeFilterConsumer<>(), DefaultAnimalFilterAcceptor.class);
//        CapabilityManager.INSTANCE.register(IMachineInfo.class, new MekfarmCapabilities.CapabilityMachineInfoConsumer<>(), DefaultMachineInfo.class);
    }
}