package mekfarm.fluids;

import mekfarm.MekfarmMod;
import mekfarm.common.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2017-02-25.
 */
public class SewageFluid extends Fluid {
    public SewageFluid() {
        super("sewage",
                new ResourceLocation(MekfarmMod.MODID, "blocks/sewage_still"),
                new ResourceLocation(MekfarmMod.MODID, "blocks/sewage_flow"));
        super.setViscosity(10000); // 7500);
        super.setDensity(50000); // 37500);
    }

    public void register() {
        FluidRegistry.registerFluid(this);
        FluidRegistry.addBucketForFluid(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerRenderer() {
        IFluidBlock block = BlocksRegistry.sewageBlock;
        Item item = Item.getItemFromBlock((Block)block);
        assert (item == Items.AIR);

        ModelBakery.registerItemVariants(item);

        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MekfarmMod.MODID + ":fluids", this.getName());

        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return modelResourceLocation;
            }
        });

        ModelLoader.setCustomStateMapper((Block) block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return modelResourceLocation;
            }
        });
    }
}
