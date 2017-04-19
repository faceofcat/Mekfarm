package mekfarm.common;

import mekfarm.MekfarmMod;
import mekfarm.blocks.DungBlock;
import mekfarm.blocks.DungBricks;
import mekfarm.machines.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CF on 2016-10-26.
 */
public final class BlocksRegistry {
    public static AnimalFarmBlock animalFarmBlock;
    public static AnimalReleaserBlock animalReleaserBlock;
    public static ElectricButcherBlock electricButcherBlock;
    public static CropFarmBlock cropFarmBlock;
    public static CropClonerBlock cropClonerBlock;
    public static AnimalGymBlock animalGymBlock;
    public static TreeFarmBlock treeFarmBlock;
    public static SewerBlock sewerBlock;
    public static LiquidXPStorageBlock xpStorageBlock;

    public static BlockFluidFinite sewageBlock;
    public static BlockFluidFinite liquidXpBlock;

    public static DungBlock dungBlock;
    public static DungBricks dungBricks;

    static void createBlocks() {
        (BlocksRegistry.animalFarmBlock = new AnimalFarmBlock()).register();
        (BlocksRegistry.animalReleaserBlock = new AnimalReleaserBlock()).register();
        (BlocksRegistry.electricButcherBlock = new ElectricButcherBlock()).register();
        (BlocksRegistry.cropFarmBlock = new CropFarmBlock()).register();
        (BlocksRegistry.cropClonerBlock = new CropClonerBlock()).register();
        (BlocksRegistry.animalGymBlock = new AnimalGymBlock()).register();
        (BlocksRegistry.treeFarmBlock = new TreeFarmBlock()).register();
        (BlocksRegistry.sewerBlock = new SewerBlock()).register();
        (BlocksRegistry.xpStorageBlock = new LiquidXPStorageBlock()).register();

        BlocksRegistry.sewageBlock = new BlockFluidFinite(FluidsRegistry.sewage, new MaterialLiquid(MapColor.BROWN)) {
            @Override
            public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
            {
                entity.setInWeb();
                if (entity instanceof EntityLivingBase)
                {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 100));
                }
            }
        };
        BlocksRegistry.sewageBlock.setRegistryName(MekfarmMod.MODID, "sewage_block");
        BlocksRegistry.sewageBlock.setCreativeTab(MekfarmMod.creativeTab);
        BlocksRegistry.sewageBlock.setRenderLayer(BlockRenderLayer.SOLID);
        GameRegistry.register(BlocksRegistry.sewageBlock);

        BlocksRegistry.liquidXpBlock = new BlockFluidFinite(FluidsRegistry.liquidXP, new MaterialLiquid(MapColor.LIME)) {
            @Override
            public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
            {
                if (entity instanceof EntityLivingBase)
                {
                    int quanta = BlocksRegistry.liquidXpBlock.getQuantaValue(world, pos);
                    if (quanta > 0) {
                        ((EntityLivingBase) entity).addPotionEffect(
                                new PotionEffect(MobEffects.REGENERATION, quanta * 100 / 15));
                    }
                }
            }
        };
        BlocksRegistry.liquidXpBlock.setRegistryName(MekfarmMod.MODID, "liquidxp_block");
        BlocksRegistry.liquidXpBlock.setCreativeTab(MekfarmMod.creativeTab);
        BlocksRegistry.liquidXpBlock.setRenderLayer(BlockRenderLayer.SOLID);
        GameRegistry.register(BlocksRegistry.liquidXpBlock);

        (BlocksRegistry.dungBlock = new DungBlock()).register();
        (BlocksRegistry.dungBricks = new DungBricks()).register();
    }
}
