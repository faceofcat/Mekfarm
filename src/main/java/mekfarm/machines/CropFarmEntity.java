package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.containers.CropFarmContainer;
import mekfarm.machines.wrappers.IPlantWrapper;
import mekfarm.machines.wrappers.ISeedWrapper;
import mekfarm.machines.wrappers.PlantWrapperFactory;
import mekfarm.ui.CropFarmContainerGUI;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by CF on 2016-11-15.
 */
public class CropFarmEntity extends BaseElectricWaterEntity<CropFarmContainer>  {
    public CropFarmEntity() {
        super(4, 500000, 3, 6, 1, 5000, CropFarmContainer.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getContainerGUI(IInventory playerInventory) {
        return new CropFarmContainerGUI(this, this.getContainer(playerInventory));
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if ((stack == null) || stack.isEmpty())
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
        EnumFacing facing = BlocksRegistry.cropFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(CropFarmBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);

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
                    ItemStack remaining = this.inStackHandler.distributeItems(lootStack, false, true);
                    if (!remaining.isEmpty()) {
                        remaining = this.outStackHandler.distributeItems(lootStack, false, false);
                    }
                    if ((remaining != null) && !remaining.isEmpty()) {
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
        for(ItemStack stack : this.inStackHandler.getCombinedInventory()) {
            ISeedWrapper seed = PlantWrapperFactory.getSeedWrapper(stack);
            if (seed != null) {
                seeds.add(seed);
            }
        }

        for(BlockPos pos : cube) {
            boolean blocked = false;
            for(IPlantWrapper blocker : blockers) {
                if (blocker.blocksNeighbour(pos)) {
                    blocked = true;
                    break;
                }
            }
            if (blocked) {
                continue;
            }

            BlockPos landPos = pos.offset(EnumFacing.DOWN);
            IBlockState state = this.getWorld().getBlockState(landPos);
            if (state != null) {
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

                    if ((plant != null) && (plantedSeed != null) && !plantedSeed.isEmpty()) {
                        if (1 == this.inStackHandler.extractFromCombinedInventory(plantedSeed, 1)) {
                            this.getWorld().setBlockState(pos, plant);
                            plantedSeed.shrink(1);
                            result += 0.2f;

                            IPlantWrapper newPlant = PlantWrapperFactory.getPlantWrapper(this.getWorld(), pos);
                            if ((newPlant != null) && newPlant.canBlockNeighbours()) {
                                blockers.add(newPlant);
                            }
                        }
                    }

                    //endregion
                }

                if (state.getBlock() == Blocks.FARMLAND) {
                    //region moisturize land

                    if (result <= 0.95f) {
                        int moisture = state.getValue(BlockFarmland.MOISTURE);
                        int fluidNeeded = Math.min(2, 7 - moisture) * 15;
                        if ((fluidNeeded > 0) && (this.fluidTank.getFluidAmount() >= fluidNeeded)) {
                            moisture = Math.min(7, moisture + 2);
                            this.getWorld().setBlockState(landPos, state.withProperty(BlockFarmland.MOISTURE, moisture));
                            this.fluidTank.drain(fluidNeeded, true, true);
                            result += 0.05f;
                        }
                    }

                    //endregion
                }
                else if ((state.getBlock() == Blocks.GRASS) || (state.getBlock() == Blocks.DIRT) || (state.getBlock() == Blocks.GRASS_PATH)) {
                    //region hoe land

                    IBlockState above = this.getWorld().getBlockState(pos);
                    if ((above != null) && (above.getBlock() == Blocks.AIR) && (result <= 0.7f)) {
                        // find hoe
                        int hoeSlot = -1;
                        for(int i = 0; i < this.inStackHandler.getSlots(); i++) {
                            ItemStack stack = this.inStackHandler.getStackInSlot(i, true);
                            if ((stack != null) && (stack.getItem() instanceof  ItemHoe)) {
                                hoeSlot = i;
                                break;
                            }
                        }

                        if (hoeSlot >= 0) {
                            // hoe land
                            this.getWorld().setBlockState(landPos, Blocks.FARMLAND.getDefaultState());
                            if (this.inStackHandler.getStackInSlot(hoeSlot, true).attemptDamageItem(1, this.getWorld().rand)) {
                                this.inStackHandler.setStackInSlot(hoeSlot, null);
                            }
                            result += 0.3f;
                        }
                    }

                    //endregion
                }
            }

            if (result > 0.95f) {
                // no more power for anything
                break;
            }
        }

        //endregion

        if (result <= .9f) {
            ItemStack fertilizer = ItemStack.EMPTY;
            for(ItemStack stack : this.inStackHandler.getCombinedInventory()) {
                if (PlantWrapperFactory.isFertilizer(stack)) {
                    fertilizer = stack.copy();
                    break;
                }
            }
            if ((fertilizer != null) && !fertilizer.isEmpty()) {
                int tries = 10;
                while ((tries >= 0) && (result <= .9f) && !fertilizer.isEmpty()) {
                    BlockPos pos = cube.getRandomInside(this.getWorld().rand);
                    IPlantWrapper plant = PlantWrapperFactory.getPlantWrapper(this.getWorld(), pos);
                    if ((plant != null) && (plant.canUseFertilizer())) {
                        int used = plant.useFertilizer(fertilizer);
                        fertilizer.shrink(this.inStackHandler.extractFromCombinedInventory(fertilizer, used));
                        result += .1;
                    }
                    tries--;
                }
            }
        }

        return result;
    }

    @Override
    protected int getFluidRequiredToWork(){
        return 30;
    }
}
