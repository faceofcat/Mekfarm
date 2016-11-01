package mekfarm.net;

import mekfarm.MekfarmMod;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by CF on 2016-10-31.
 */
public class MekfarmPackets implements IMekfarmPackets {
    private static SimpleNetworkWrapper wrapper = new SimpleNetworkWrapper("mekfarm");

    public MekfarmPackets() {
        MekfarmPackets.wrapper.registerMessage(SimpleNBTHandler.class, SimpleNBTMessage.class, 1, Side.CLIENT);
        MekfarmPackets.wrapper.registerMessage(SimpleNBTHandler.class, SimpleNBTMessage.class, 1, Side.SERVER);
    }

    @Override
    public void send(IMessage message) {
//        if (MekfarmMod.getSide() == Side.SERVER) {
//            if (message instanceof SimpleNBTMessage) {
//                SimpleNBTMessage msg = ((SimpleNBTMessage)message);
//                BlockPos pos = msg.getPos();
//
//                // send to 12 chunks around? :S
//                MekfarmPackets.wrapper.sendToAllAround(message, new NetworkRegistry.TargetPoint(msg.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16 * 12));
//            } else {
                MekfarmPackets.wrapper.sendToAll(message);
//            }
//        }
//        if (MekfarmMod.getSide() == Side.CLIENT) {
//            MekfarmPackets.wrapper.sendToServer(message);
//        }
    }
}
