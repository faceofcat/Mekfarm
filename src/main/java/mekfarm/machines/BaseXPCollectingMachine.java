package mekfarm.machines;

import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.FluidsRegistry;
import mekfarm.common.ILiquidXPCollector;
import mekfarm.items.LiquidXPCollectorItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.FilteredFluidTank;
import net.ndrei.teslacorelib.inventory.FluidTank;

/**
 * Created by CF on 2017-03-08.
 */
public abstract class BaseXPCollectingMachine extends ElectricMekfarmMachine implements ILiquidXPCollector {
    protected IFluidTank xpTank = null;

    protected BaseXPCollectingMachine(int typeId) {
        super(typeId);
    }

    @Override
    public boolean hasXPCollector() {
        return this.hasAddon(LiquidXPCollectorItem.class);
    }

    @Override
    public void onLiquidXPAddonAdded(ItemStack stack) {
        if (this.xpTank != null) {
            super.removeFluidTank(EnumDyeColor.LIME, this.xpTank);
            this.xpTank = null;
        }

        this.xpTank = new FilteredFluidTank(FluidsRegistry.liquidXP, new FluidTank(1000) {
            @Override
            protected void onContentsChanged() {
                int amount = this.getFluidAmount();
                if (amount > 0) {
                    ItemStack stack = BaseXPCollectingMachine.this.getAddonStack(LiquidXPCollectorItem.class);
                    if (!ItemStackUtil.isEmpty(stack)) {
                        stack.setTagInfo("StoredLiquidXP", new NBTTagInt(this.getFluidAmount()));
                    }
                } else {
                    ItemStack stack = BaseXPCollectingMachine.this.getAddonStack(LiquidXPCollectorItem.class);
                    if (!ItemStackUtil.isEmpty(stack)) {
                        NBTTagCompound nbt = stack.getTagCompound();
                        if ((nbt != null) && (nbt.hasKey("StoredLiquidXP", Constants.NBT.TAG_INT))) {
                            nbt.removeTag("StoredLiquidXP");
                        }
                    }
                }
            }
        });
        super.addFluidTank(this.xpTank, EnumDyeColor.LIME, "Liquid XP",
                new BoundingRectangle(151, 25, 18, 54));
        int xp = LiquidXPCollectorItem.getStoredXP(stack);
        if (xp > 0) {
            this.xpTank.fill(new FluidStack(FluidsRegistry.liquidXP, xp), true);
        }

        BasicTeslaGuiContainer.refreshParts(this.getWorld());
    }

    @Override
    public void onLiquidXPAddonRemoved(ItemStack stack) {
        if (this.xpTank != null) {
            super.removeFluidTank(EnumDyeColor.LIME, this.xpTank);
            this.xpTank = null;

            BasicTeslaGuiContainer.refreshParts(this.getWorld());
        }
    }

    @Override
    protected final float performWork() {
        float result = this.performWorkInternal() / 1.25f;

        if ((result <= .800001) && super.hasAddon(LiquidXPCollectorItem.class) && (this.xpTank != null)) {
            boolean orbCollected = false;
            for(EntityXPOrb orb : this.getXPOrbLookupCube().findEntities(EntityXPOrb.class, this.getWorld())) {
                if (this.xpTank.getCapacity() <= this.xpTank.getFluidAmount()) {
                    break;
                }

                if (0 < this.xpTank.fill(new FluidStack(FluidsRegistry.liquidXP, orb.getXpValue()), true)) {
                    this.getWorld().removeEntity(orb);
                    orbCollected = true;
                }
            }
            if (orbCollected) {
                result += .2f;
            }
        }

        return Math.min(1.0f, result);
    }

    protected BlockCube getXPOrbLookupCube() {
        EnumFacing facing = super.getFacing();
        return BlockPosUtils.getCube(this.getPos(), facing.getOpposite(), 4, 1);
    }

    protected abstract float performWorkInternal();

    @Override
    protected BoundingRectangle getInputInventoryBounds(int columns, int rows) {
        BoundingRectangle area = super.getInputInventoryBounds(columns, rows);
        return new BoundingRectangle(area.getLeft() - 18, area.getTop(), area.getWidth(), area.getHeight());
    }

    @Override
    protected BoundingRectangle getOutputInventoryBounds(int columns, int rows) {
        BoundingRectangle area = super.getOutputInventoryBounds(columns, rows);
        return new BoundingRectangle(area.getLeft() - 18, area.getTop(), area.getWidth(), area.getHeight());
    }
}
