package mekfarm.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mekfarm.MekfarmMod;
import mekfarm.common.FluidsRegistry;
import mekfarm.common.ILiquidXPCollector;
import mekfarm.common.ItemsRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;
import net.ndrei.teslacorelib.items.BaseAddon;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by CF on 2017-03-08.
 */
public class LiquidXPCollectorItem extends BaseAddon {
    public static final int MAX_CAPACITY = 1000;

    public LiquidXPCollectorItem() {
        super(MekfarmMod.MODID, MekfarmMod.creativeTab, "liquidxp_collector");
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                " x ", "gyg", "bbb",
                'x', Items.EXPERIENCE_BOTTLE,
                'g', "gearIron", // TeslaCoreLib.gearIron,
                'y', TeslaCoreLib.baseAddon,
                'b', Items.GLASS_BOTTLE);
    }

    @Override
    public boolean canBeAddedTo(SidedTileEntity machine) {
        return (machine != null) && (machine instanceof ILiquidXPCollector) && !((ILiquidXPCollector) machine).hasXPCollector();
    }

    @Override
    public float getWorkEnergyMultiplier() {
        return 1.25f;
    }

    @Override
    public void onAdded(ItemStack stack, SidedTileEntity machine) {
        super.onAdded(stack, machine);

        if (machine instanceof ILiquidXPCollector) {
            ((ILiquidXPCollector) machine).onLiquidXPAddonAdded(stack);
        }
    }

    @Override
    public void onRemoved(ItemStack stack, SidedTileEntity machine) {
        super.onRemoved(stack, machine);

        if (machine instanceof ILiquidXPCollector) {
            ((ILiquidXPCollector) machine).onLiquidXPAddonRemoved(stack);
        }
    }

//    @Override
//    public boolean isDamageable()  {
//        return true;
//    }
//
//    @Override
//    public int getDamage(ItemStack stack) {
//        NBTTagCompound nbt = stack.getTagCompound();
//        if ((nbt != null) && (nbt.hasKey("StoredLiquidXP", Constants.NBT.TAG_INT))) {
//            return Math.min(5000, nbt.getInteger("StoredLiquidXP"));
//        }
//        return 0;
//    }
//
//    @Override
//    public int getMaxDamage(ItemStack stack) {
//        return 5000;
//    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);

        NBTTagCompound nbt = ItemStackUtil.isEmpty(stack) ? null : stack.getTagCompound();
        if ((nbt != null) && nbt.hasKey("StoredLiquidXP", Constants.NBT.TAG_INT)) {
            tooltip.add(ChatFormatting.DARK_GREEN + "Stored XP: " + ChatFormatting.GREEN + nbt.getInteger("StoredLiquidXP"));
        } else {
            tooltip.add(ChatFormatting.DARK_GRAY + "No XP Stored");
        }
    }

    public static int getStoredXP(ItemStack stack) {
        if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() == ItemsRegistry.liquidXPCollectorItem)) {
            NBTTagCompound nbt = stack.getTagCompound();
            if ((nbt != null) && (nbt.hasKey("StoredLiquidXP", Constants.NBT.TAG_INT))) {
                return nbt.getInteger("StoredLiquidXP");
            }
        }
        return 0;
    }

    public static void setStoredXP(ItemStack stack, int storedXp) {
        if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() == ItemsRegistry.liquidXPCollectorItem)) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) {
                stack.setTagCompound(nbt = new NBTTagCompound());
            }
            if (storedXp > 0) {
                nbt.setInteger("StoredLiquidXP", storedXp);
            }
            else if (nbt.hasKey("StoredLiquidXP", Constants.NBT.TAG_INT)) {
                nbt.removeTag("StoredLiquidXP");
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new LiquidXPHolderCapability(stack);
    }

    private class LiquidXPHolderCapability implements ICapabilityProvider, IFluidHandler {
        private ItemStack stack;

        public LiquidXPHolderCapability(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
                return (T)this;
            }
            return null;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[] {
                new FluidTankProperties(new FluidStack(FluidsRegistry.liquidXP, LiquidXPCollectorItem.getStoredXP(this.stack)),
                        LiquidXPCollectorItem.MAX_CAPACITY, true, true)
            };
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if ((resource == null) || (resource.amount == 0) || (resource.getFluid() != FluidsRegistry.liquidXP)) {
                return 0;
            }

            int existing = LiquidXPCollectorItem.getStoredXP(this.stack);
            int filled = Math.min(LiquidXPCollectorItem.MAX_CAPACITY - existing, resource.amount);
            if (doFill) {
                LiquidXPCollectorItem.setStoredXP(this.stack, existing + filled);
            }
            return filled;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if ((resource == null) || (resource.getFluid() != FluidsRegistry.liquidXP)) {
                return null;
            }

            return this.drain(resource.amount, doDrain);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            int existing = LiquidXPCollectorItem.getStoredXP(this.stack);
            if ((maxDrain == 0) || (existing == 0)) {
                return null;
            }

            int canDrain = Math.min(existing, maxDrain);
            if (doDrain) {
                LiquidXPCollectorItem.setStoredXP(this.stack, existing - canDrain);
            }
            return new FluidStack(FluidsRegistry.liquidXP, canDrain);
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        int xp = LiquidXPCollectorItem.getStoredXP(stack);
        return (xp == 0) ? 16 : 4;
    }
}
