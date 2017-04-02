package mekfarm.machines;

import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.FluidsRegistry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

/**
 * Created by CF on 2017-02-28.
 */
public class SewerEntity extends BaseXPCollectingMachine {
    private IFluidTank sewageTank;

    public SewerEntity() {
        super(SewerEntity.class.getName().hashCode());
    }

    //#region inventory management

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.sewageTank = super.addFluidTank(FluidsRegistry.sewage, 10000, EnumDyeColor.BROWN, "Sewage Tank",
                new BoundingRectangle(43, 25, 18, 54));
        this.sewageTank.fill(new FluidStack(FluidsRegistry.sewage, 2000), true);
    }

    @Override
    protected int getInputSlots() {
        return 0;
    }

    @Override
    protected int getOutputSlots() {
        return 0;
    }

    @Override
    protected boolean acceptsFluidItem(ItemStack stack) {
        if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (handler != null) {
                if (1 == handler.fill(new FluidStack(FluidsRegistry.sewage, 1), false)) {
                    return true;
                }
            }
        }

        return (stack.getItem() == Items.BUCKET);
    }

    @Override
    protected void processFluidItems(ItemStackHandler fluidItems) {
        if (fluidItems != null) {
            ItemStack stack = fluidItems.getStackInSlot(0);
            ItemStack outputStack = fluidItems.getStackInSlot(1);
            if (!ItemStackUtil.isEmpty(stack) && ItemStackUtil.isEmpty(outputStack)) {
                ItemStack input = ItemStackUtil.copyWithSize(stack, 1);
                FluidActionResult result = FluidUtil.tryFillContainer(input, this.fluidHandler, Fluid.BUCKET_VOLUME, null, true);
                if (result.isSuccess()) {
                    ItemStackUtil.shrink(stack, 1);
                    if (stack.isEmpty()) {
                        fluidItems.setStackInSlot(0, ItemStackUtil.getEmptyStack());
                    }
                    fluidItems.setStackInSlot(1, result.getResult());
                }
            }
        }
    }

    //#endregion

    @Override
    protected int getEnergyForWork() {
        return 20;
    }

    @Override
    protected int getEnergyForWorkRate() {
        return 1;
    }

    @Override
    protected float performWorkInternal() {
        BlockCube cube = this.getWorkArea(EnumFacing.UP, 2);
        int sewage = 0;
        for (EntityAnimal animal : this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, cube.getBoundingBox())) {
            sewage += Math.round(animal.getMaxHealth());
        }
        if (sewage > 0) {
            this.sewageTank.fill(new FluidStack(FluidsRegistry.sewage, sewage), true);
        }
        return 1.0f;
    }

    @Override
    protected BlockCube getXPOrbLookupCube() {
        return BlockPosUtils.getCube(this.getPos(), EnumFacing.UP, 4, 3);
    }
}
