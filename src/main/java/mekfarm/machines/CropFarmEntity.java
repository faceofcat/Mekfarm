package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.FakeMekPlayer;
import mekfarm.containers.CropFarmContainer;
import mekfarm.ui.CropFarmContainerGUI;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.List;

/**
 * Created by CF on 2016-11-15.
 */
public class CropFarmEntity extends BaseElectricWaterEntity<CropFarmContainer, CropFarmContainerGUI>  {
    public CropFarmEntity() {
        super(4, 500000, 3, 6, 1, 5000, CropFarmContainer.class, CropFarmContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        // test for hoe
        if (stack.getItem() instanceof ItemHoe) {
            return true;
        }

        if (stack.getItem() instanceof IPlantable) {
            return true;
        }

        return false;
    }

    @Override
    protected float performWork() {
        float result = 0.0f;
        EnumFacing facing = BlocksRegistry.cropFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(CropFarmBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

//        MekfarmMod.logger.info("------------------------------");
        //region break plants

        for(long x = Math.round(aabb.minX); x < aabb.maxX; x++) {
            for (long z = Math.round(aabb.minZ); z < aabb.maxZ; z++) {
                BlockPos pos = new BlockPos(x, aabb.minY, z);
                IBlockState state = this.getWorld().getBlockState(pos);
                if (state.getBlock() instanceof IGrowable) {
                    IGrowable growable = (IGrowable)state.getBlock();
                    if (growable.canGrow(this.getWorld(), pos, state, false) == false) {
                        FakeMekPlayer player = MekfarmMod.getFakePlayer(this.getWorld());
                        state.getBlock().harvestBlock(this.getWorld(), player, pos, state, null, null);
                        this.getWorld().setBlockState(pos, state.getBlock().getDefaultState());
                        this.getWorld().destroyBlock(pos, true); // <-- to force replanting
                        result += 0.45f;
//                        MekfarmMod.logger.info("harvested: " + state.toString() + ", result: " + result);
                    }
                }

                if (result > (1 - 0.45f)) {
                    break;
                }
            }
            if (result > (1 - 0.45f)) {
                break;
            }
        }

        //endregion

        //region collect loot

        List<EntityItem> items = this.getWorld().getEntitiesWithinAABB(EntityItem.class, aabb);
        boolean pickedUpLoot = false;
        if ((result <= .9f) && (items.isEmpty() == false)) {
            for (EntityItem item: items) {
                ItemStack original = item.getEntityItem();
                ItemStack remaining = original;
                if (this.acceptsInputStack(0, remaining, false)) {
                    remaining = this.inStackHandler.distributeItems(remaining, false);
                }
                if ((remaining != null) && (remaining.stackSize > 0)) {
                    remaining = this.outStackHandler.distributeItems(remaining, false);
                }
                if ((remaining == null) || (remaining.stackSize == 0)) {
                    this.getWorld().removeEntity(item);
                    pickedUpLoot = true;
                }
                else if (remaining.stackSize != original.stackSize) {
                    item.setEntityItemStack(remaining);
                    pickedUpLoot = true;
                }
            }
        }
        if (pickedUpLoot) {
            result += .1f;
//            MekfarmMod.logger.info("picked loot, result: " + result);
        }

        //endregion

        //region water land & plant things

        for(BlockPos pos : cube) {
            BlockPos landPos = pos.offset(EnumFacing.DOWN);
            IBlockState state = this.getWorld().getBlockState(landPos);
            if (state != null) {
                if (state.getBlock() == Blocks.FARMLAND) {
                    //region plant thing

                    IBlockState plant = this.getWorld().getBlockState(pos);
                    if ((plant != null) && (plant.getBlock() == Blocks.AIR) && (result <= 0.8f)) {
                        plant = null;
                        int inputSlot = -1;
                        for (int i = 0; i < this.inStackHandler.getSlots(); i++) {
                            ItemStack temp = this.inStackHandler.getStackInSlot(i, true);
                            if ((temp != null) && (temp.stackSize > 0)) {
                                Item tempItem = temp.getItem();
                                if ((tempItem instanceof IPlantable) && (((IPlantable) tempItem).getPlantType(this.getWorld(), pos) == EnumPlantType.Crop)) {
                                    plant = ((IPlantable) tempItem).getPlant(this.getWorld(), pos);
                                    inputSlot = i;
                                    break;
                                }
                            }
                        }

                        if ((plant != null) && (inputSlot >= 0)) {
                            this.inStackHandler.extractItem(inputSlot, 1, false, true);
                            this.getWorld().setBlockState(pos, plant);
                            result += 0.2f;
//                            MekfarmMod.logger.info("planted: " + plant.toString() + ", result: " + result);
                        }
                    }

                    //endregion
                    //region moisturize land

                    if (result <= 0.95f) {
                        int moisture = state.getValue(BlockFarmland.MOISTURE);
                        int fluidNeeded = Math.min(2, 7 - moisture) * 15;
                        if ((fluidNeeded > 0) && (this.fluidTank.getFluidAmount() >= fluidNeeded)) {
                            moisture = Math.min(7, moisture + 2);
                            this.getWorld().setBlockState(landPos, state.withProperty(BlockFarmland.MOISTURE, moisture));
                            this.fluidTank.drain(fluidNeeded, true, true);
                            result += 0.05f;
//                            MekfarmMod.logger.info("moisturized: " + fluidNeeded + "mb at " + pos.toString() + ", result: " + result);
                        }
                    }

                    //endregion
                }
                else if ((state.getBlock() == Blocks.GRASS) || (state.getBlock() == Blocks.DIRT) || (state.getBlock() == Blocks.GRASS_PATH)) {
                    IBlockState above = this.getWorld().getBlockState(pos);
                    if ((above != null) && (above.getBlock() == Blocks.AIR) && (result <= 0.7f)) {
                        int hoeSlot = -1;

                        for(int i = 0; i < this.inStackHandler.getSlots(); i++) {
                            ItemStack stack = this.inStackHandler.getStackInSlot(i, true);
                            if ((stack != null) && (stack.getItem() instanceof  ItemHoe)) {
                                hoeSlot = i;
                                break;
                            }
                        }

                        if (hoeSlot >= 0) {
                            this.getWorld().setBlockState(landPos, Blocks.FARMLAND.getDefaultState());
                            if (this.inStackHandler.getStackInSlot(hoeSlot, true).attemptDamageItem(1, this.getWorld().rand)) {
                                this.inStackHandler.setStackInSlot(hoeSlot, null);
                            }
                            result += 0.3f;
                        }
                    }
                }
            }

            if (result > 0.95f) {
                // no more power for anything
                break;
            }
        }

        //endregion
//        MekfarmMod.logger.info("------------------------------: " + result);

        return result;
    }

    @Override
    protected int getFluidRequiredToWork(){
        return 30;
    }
}
