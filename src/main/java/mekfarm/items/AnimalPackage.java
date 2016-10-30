package mekfarm.items;

import mekfarm.MekfarmMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by CF on 2016-10-30.
 */
public class AnimalPackage extends Item {
    public AnimalPackage() {
        super();

        this.setRegistryName("animal_package");
        this.setUnlocalizedName(MekfarmMod.MODID + ".animal_package");
        this.setCreativeTab(CreativeTabs.FOOD);
    }
}
