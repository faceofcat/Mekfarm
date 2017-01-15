package mekfarm.items;

import mekfarm.MekfarmMod;
import net.ndrei.teslacorelib.items.RegisteredItem;

/**
 * Created by CF on 2016-11-10.
 */
public abstract class BaseItem extends RegisteredItem {
    public BaseItem(String registryName) {
        super(MekfarmMod.MODID, MekfarmMod.creativeTab, registryName);

//        this.setRegistryName(registryName);
//        this.setUnlocalizedName(MekfarmMod.MODID + "_" + registryName);
//        this.setCreativeTab(MekfarmMod.creativeTab);
//
//        IRecipe recipe = this.getRecipe();
//        if (recipe != null) {
//            CraftingManager.getInstance().addRecipe(recipe);
//        }
    }

//    protected IRecipe getRecipe() {
//        return null;
//    }
}
