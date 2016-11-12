package mekfarm.items;

import mekfarm.MekfarmMod;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/**
 * Created by CF on 2016-11-10.
 */
public abstract class BaseItem extends Item {
    public BaseItem(String registryName) {
        super();

        this.setRegistryName(registryName);
        this.setUnlocalizedName(MekfarmMod.MODID + "_" + registryName);
        this.setCreativeTab(MekfarmMod.creativeTab);

        IRecipe recipe = this.getRecipe();
        if (recipe != null) {
            CraftingManager.getInstance().addRecipe(recipe);
        }
    }

    protected IRecipe getRecipe() {
        return null;
    }
}
