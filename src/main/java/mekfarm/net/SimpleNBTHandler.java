package mekfarm.net;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by CF on 2016-11-01.
 */
public class SimpleNBTHandler implements IMessageHandler<SimpleNBTMessage, SimpleNBTMessage> {
    @Override
    public SimpleNBTMessage onMessage(SimpleNBTMessage message, MessageContext ctx) {
        if (ctx.side.isClient() == true) {
            // process client side message
            if ((message != null) && (message.getPos() != null) && (Minecraft.getMinecraft() != null)
                    && (Minecraft.getMinecraft().world != null)) {
                TileEntity entity = Minecraft.getMinecraft().world.getTileEntity(message.getPos());
                if ((entity != null) && (entity instanceof  ISimpleNBTMessageHandler)) {
                    return ((ISimpleNBTMessageHandler)entity).handleMessage(message);
                }
            }
        } else {
            // process server side message
        }
        return null;
    }
}
