package mekfarm.inventories;

import net.minecraftforge.items.IItemHandler;

/**
 * Created by CF on 2016-11-11.
 */
public interface IInputOutputItemHandler extends IItemHandler {
    int getInputSlots();
    int getOutputSlots();
}
