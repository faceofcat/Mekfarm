package mekfarm.items;

import mekfarm.MekfarmMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by CF on 2016-10-26.
 */
public final class FarmItem extends Item {
    public FarmItem() {
        super();

        this.setRegistryName("farm_cow");
        this.setUnlocalizedName(MekfarmMod.MODID + ".farm_cow");
        this.setCreativeTab(CreativeTabs.FOOD);
    }
}
