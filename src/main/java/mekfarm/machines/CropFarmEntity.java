package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.FakeMekPlayer;
import mekfarm.containers.CropFarmContainer;
import mekfarm.inventories.SingleFluidTank;
import mekfarm.ui.CropFarmContainerGUI;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.List;

/**
 * Created by CF on 2016-11-15.
 */
public class CropFarmEntity extends BaseElectricEntity<CropFarmContainer, CropFarmContainerGUI>  {
    public CropFarmEntity() {
        super(4, 500000, 3, 6, 1, CropFarmContainer.class, CropFarmContainerGUI.class);
    }

    private SingleFluidTank fluidTank = new SingleFluidTank(5000) {
    };

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        EnumFacing machineFacing = this.getBlockType().getStateFromMeta(this.getBlockMetadata())
                .getValue(BaseOrientedBlock.FACING);
        Boolean isFront = (machineFacing == facing);

        if (!isFront && (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T>T getCapability(Capability<T> capability, EnumFacing facing) {
        EnumFacing machineFacing = this.getBlockType().getStateFromMeta(this.getBlockMetadata())
                .getValue(BaseOrientedBlock.FACING);
        Boolean isFront = (machineFacing == facing);

        if (!isFront && (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return (T)this.fluidTank;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    protected float performWork() {
        float result = 0.0f;
        EnumFacing facing = BlocksRegistry.cropFarmBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(CropFarmBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

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
                        MekfarmMod.logger.info("harvested: " + state.toString());
                        result += 0.45f;
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

        //region collect loot

        List<EntityItem> items = this.getWorld().getEntitiesWithinAABB(EntityItem.class, aabb);
        boolean pickedUpLoot = false;
        if (items.isEmpty() == false) {
            for (EntityItem item: items) {
                ItemStack original = item.getEntityItem();
                ItemStack remaining = this.outStackHandler.insertItems(original, false);
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
        }

        //endregion

        return result;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        NBTTagCompound fluid = new NBTTagCompound();
        fluid = this.fluidTank.writeToNBT(fluid);
        compound.setTag("fluid", fluid);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("fluid")) {
            this.fluidTank.readFromNBT(compound.getCompoundTag("fluid"));
        }
    }
}
