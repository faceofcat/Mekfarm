package mekfarm.blocks;

import mekfarm.MekfarmMod;
import mekfarm.common.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2017-04-19.
 */
public class DungBricks extends Block {
    public DungBricks() {
        super(Material.ROCK);

        this.setRegistryName(MekfarmMod.MODID, "dung_bricks");
        this.setUnlocalizedName(MekfarmMod.MODID + "_dung_bricks");
        this.setCreativeTab(MekfarmMod.creativeTab);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(3.0f);
    }

    public void register() {
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), this.getRegistryName());

        CraftingManager.getInstance().addRecipe(new ShapedOreRecipe(
                new ItemStack(Item.getItemFromBlock(this), 4),
                "bb",
                "bb",
                'b', BlocksRegistry.dungBlock
        ));
    }
}
