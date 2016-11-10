package mekfarm.items;

import mekfarm.MekfarmMod;
import net.minecraft.item.Item;

/**
 * Created by CF on 2016-11-10.
 */
public abstract class BaseItem extends Item {
    public BaseItem(String registryName) {
        super();

        this.setRegistryName(registryName);
        this.setUnlocalizedName(MekfarmMod.MODID + "_" + registryName);
        this.setCreativeTab(MekfarmMod.creativeTab);
    }
}
