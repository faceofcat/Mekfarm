package mekfarm.machines;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.ArrayList;

/**
 * Created by CF on 2016-10-28.
 */
public class TreeFarmEntity extends ElectricMekfarmMachine {
    private static ArrayList<Item> acceptedItems = new ArrayList<>();

    static {
        TreeFarmEntity.acceptedItems.add(Items.SHEARS);
        TreeFarmEntity.acceptedItems.add(Item.getItemFromBlock(Blocks.SAPLING));
    }

    public TreeFarmEntity() {
        super(TreeFarmEntity.class.getName().hashCode());
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack))
            return true;

        return TreeFarmEntity.acceptedItems.contains(stack.getItem());
    }

    @Override
    protected float performWork() {
        return 0.0f;
    }
}
