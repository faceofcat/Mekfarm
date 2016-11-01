package mekfarm.net;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by CF on 2016-11-01.
 */
public interface IMekfarmPackets {
    void send(IMessage message);
}
