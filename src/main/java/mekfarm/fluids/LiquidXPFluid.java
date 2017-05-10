package mekfarm.fluids;

import mekfarm.MekfarmMod;
import mekfarm.common.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CF on 2017-03-09.
 */
public class LiquidXPFluid extends Fluid {
    public LiquidXPFluid() {
        super("liquidxp",
                new ResourceLocation(MekfarmMod.MODID, "blocks/liquidxp_still"),
                new ResourceLocation(MekfarmMod.MODID, "blocks/liquidxp_flow"));
        super.setLuminosity(20);
        super.setDensity(800);
        super.setViscosity(1500);
    }

    public void register() {
        FluidRegistry.registerFluid(this);
        FluidRegistry.addBucketForFluid(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerRenderer() {
        IFluidBlock block = BlocksRegistry.liquidXpBlock;
//        Item item = Item.getItemFromBlock((Block)block);
//        assert (item == Items.AIR);
//
//        ModelBakery.registerItemVariants(item);

        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MekfarmMod.MODID + ":fluids", this.getName());

//        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
//            @Override
//            public ModelResourceLocation getModelLocation(ItemStack stack) {
//                return modelResourceLocation;
//            }
//        });

        ModelLoader.setCustomStateMapper((Block) block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return modelResourceLocation;
            }
        });
    }
}
