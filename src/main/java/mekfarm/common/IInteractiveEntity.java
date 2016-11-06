package mekfarm.common;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by CF on 2016-11-06.
 */
public interface IInteractiveEntity {
    boolean canInteractWith(EntityPlayer playerIn);
}
