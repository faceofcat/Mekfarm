package mekfarm.common;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

/**
 * Created by CF on 2016-11-06.
 */
public interface IContainerProvider {
    Container getContainer(IInventory playerInventory);
    GuiContainer getContainerGUI(IInventory playerInventory);
}
