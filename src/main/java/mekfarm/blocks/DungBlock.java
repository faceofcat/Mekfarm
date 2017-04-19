package mekfarm.blocks;

import mekfarm.MekfarmMod;
import mekfarm.common.FluidsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CF on 2017-04-19.
 */
public class DungBlock extends Block {
    public DungBlock() {
        super(Material.ROCK);

        this.setRegistryName(MekfarmMod.MODID, "dung_block");
        this.setUnlocalizedName(MekfarmMod.MODID + "_dung_block");
        this.setCreativeTab(MekfarmMod.creativeTab);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(1.0f);
    }

    public void register() {
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), this.getRegistryName());

        CraftingManager.getInstance().addRecipe(new ShapedOreRecipe(
                new ItemStack(Item.getItemFromBlock(this), 9),
                "hs",
                "wc",
                'h', Blocks.HAY_BLOCK,
                's', UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, FluidsRegistry.sewage),
                'c', Blocks.CLAY,
                'w', Items.WATER_BUCKET
        ));
    }
}
