package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.machines.wrappers.IPlantWrapper;
import mekfarm.machines.wrappers.ISeedWrapper;
import mekfarm.machines.wrappers.PlantWrapperFactory;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

import java.util.List;

/**
 * Created by CF on 2016-11-15.
 */
public class CropFarmEntity extends ElectricMekfarmMachine {
    private IFluidTank waterTank;

    public CropFarmEntity() {
        super(CropFarmEntity.class.hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.waterTank = super.addFluidTank(FluidRegistry.WATER, 5000, EnumDyeColor.BLUE, "Water Tank",
                new BoundingRectangle(43, 25, 18, 54));
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack))
            return true;

        // test for hoe
        if (stack.getItem() instanceof ItemHoe) {
            return true;
        }

        return PlantWrapperFactory.isFertilizer(stack) || PlantWrapperFactory.isSeed(stack);
    }

    @Override
    protected float performWork() {
        float result = 0.0f;
        EnumFacing facing = super.getFacing();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing.getOpposite(), 3, 1);

        List<IPlantWrapper> blockers = Lists.newArrayList();

        //region harvest plants

        for(BlockPos pos : cube) {
            IPlantWrapper plant = PlantWrapperFactory.getPlantWrapper(this.getWorld(), pos);
            if (plant == null) {
                continue;
            }

            if (plant.canBeHarvested() && ((1.0f - result) >= .45f)) {
                List<ItemStack> loot = plant.harvest(0);
                for(ItemStack lootStack : loot) {
                    ItemStack remaining = ItemHandlerHelper.insertItem(this.inStackHandler, lootStack, false);
                    if (!ItemStackUtil.isEmpty(remaining)) {
                        remaining = ItemHandlerHelper.insertItem(this.outStackHandler, lootStack, false);
                    }
                    if (!ItemStackUtil.isEmpty(remaining)) {
                        BlockPos spawnPos = this.pos.offset(facing);
                        world.spawnEntity(new EntityItem(this.getWorld(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), remaining));
                    }
                }
                result += 0.45f;
            }
            else if (plant.canBlockNeighbours()) {
                blockers.add(plant);
            }
        }

        //endregion

        //region water land & plant things

        List<ISeedWrapper> seeds = Lists.newArrayList();
        for(ItemStack stack : ItemStackUtil.getCombinedInventory(this.inStackHandler)) {
            ISeedWrapper seed = PlantWrapperFactory.getSeedWrapper(stack);
            if (seed != null) {
                seeds.add(seed);
            }
        }

        for(BlockPos pos : cube) {
            boolean blocked = false;
            for (IPlantWrapper blocker : blockers) {
                if (blocker.blocksNeighbour(pos)) {
                    blocked = true;
                    break;
                }
            }
            if (blocked) {
                continue;
            }

            BlockPos landPos = pos.offset(EnumFacing.DOWN);
            if (this.getWorld().isAirBlock(pos) && (result <= 0.8f)) {
                //region plant thing

                IBlockState plant = null;
                ItemStack plantedSeed = null;
                for (ISeedWrapper seed : seeds) {
                    if (seed.canPlantHere(this.getWorld(), pos)) {
                        plant = seed.plant(this.getWorld(), pos);
                        plantedSeed = seed.getSeeds();
                        break;
                    }
                }

                if ((plant != null) && !ItemStackUtil.isEmpty(plantedSeed)) {
                    if (1 == ItemStackUtil.extractFromCombinedInventory(this.inStackHandler, plantedSeed, 1)) {
                        this.getWorld().setBlockState(pos, plant);
                        ItemStackUtil.shrink(plantedSeed, 1);
                        result += 0.2f;

                        IPlantWrapper newPlant = PlantWrapperFactory.getPlantWrapper(this.getWorld(), pos);
                        if ((newPlant != null) && newPlant.canBlockNeighbours()) {
                            blockers.add(newPlant);
                        }
                    }
                }

                //endregion
            }

            IBlockState state = this.getWorld().getBlockState(landPos);
            if (state.getBlock() == Blocks.FARMLAND) {
                //region moisturize land

                if (result <= 0.95f) {
                    int moisture = state.getValue(BlockFarmland.MOISTURE);
                    int fluidNeeded = Math.min(2, 7 - moisture) * 15;
                    if ((fluidNeeded > 0) && (this.waterTank.getFluidAmount() >= fluidNeeded)) {
                        moisture = Math.min(7, moisture + 2);
                        this.getWorld().setBlockState(landPos, state.withProperty(BlockFarmland.MOISTURE, moisture));
                        this.waterTank.drain(fluidNeeded, true);
                        result += 0.05f;
                    }
                }

                //endregion
            } else if ((state.getBlock() == Blocks.GRASS) || (state.getBlock() == Blocks.DIRT) || (state.getBlock() == Blocks.GRASS_PATH)) {
                //region hoe land

                if (this.getWorld().isAirBlock(pos) && (result <= 0.7f)) {
                    // find hoe
                    int hoeSlot = -1;
                    for (int i = 0; i < this.inStackHandler.getSlots(); i++) {
                        ItemStack stack = this.inStackHandler.getStackInSlot(i);
                        if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() instanceof ItemHoe)) {
                            hoeSlot = i;
                            break;
                        }
                    }

                    if (hoeSlot >= 0) {
                        // hoe land
                        this.getWorld().setBlockState(landPos, Blocks.FARMLAND.getDefaultState());
                        if (this.inStackHandler.getStackInSlot(hoeSlot).attemptDamageItem(1, this.getWorld().rand)) {
                            this.inStackHandler.setStackInSlot(hoeSlot, ItemStackUtil.getEmptyStack());
                        }
                        result += 0.3f;
                    }
                }

                //endregion
            }

            if (result > 0.95f) {
                // no more power for anything
                break;
            }
        }

        //endregion

        if (result <= .9f) {
            ItemStack fertilizer = ItemStackUtil.getEmptyStack();
            for(ItemStack stack : ItemStackUtil.getCombinedInventory(this.inStackHandler)) {
                if (PlantWrapperFactory.isFertilizer(stack)) {
                    fertilizer = stack.copy();
                    break;
                }
            }
            if (!ItemStackUtil.isEmpty(fertilizer)) {
                int tries = 10;
                while ((tries >= 0) && (result <= .9f) && !ItemStackUtil.isEmpty(fertilizer)) {
                    BlockPos pos = cube.getRandomInside(this.getWorld().rand);
                    IPlantWrapper plant = PlantWrapperFactory.getPlantWrapper(this.getWorld(), pos);
                    if ((plant != null) && (plant.canUseFertilizer())) {
                        int used = plant.useFertilizer(fertilizer);
                        ItemStackUtil.shrink(fertilizer, ItemStackUtil.extractFromCombinedInventory(this.inStackHandler, fertilizer, used));
                        result += .1;
                    }
                    tries--;
                }
            }
        }

        return result;
    }
}
