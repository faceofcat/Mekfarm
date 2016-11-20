package mekfarm.common;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

/**
 * Created by CF on 2016-11-12.
 *
 * Inspired from: CoFHCore/src/main/java/cofh/core/entity/CoFHFakePlayer.java
 */
public class FakeMekPlayer extends FakePlayer {
    public ItemStack previousItem = null;
//    public String myName = "[MEK]";

    public FakeMekPlayer(WorldServer world) {
        super(world, new GameProfile(UUID.randomUUID(), ""));
        this.addedToChunk = false;
        this.onGround = true;
    }

    public void setItemInHand(ItemStack stack) {
        super.setHeldItem(EnumHand.MAIN_HAND, stack);
    }

    public void setItemInUse(ItemStack stack) {
        this.setItemInHand(stack);
        super.ticksSinceLastSwing = Math.round(super.getCooldownPeriod()) + 1;
        this.onUpdate();
    }

    @Override
    public void onUpdate() {
        ItemStack itemStackOld = this.previousItem;
        ItemStack itemStackCurrent = super.getHeldItemMainhand();

        if (ItemStack.areItemStacksEqual(itemStackCurrent, itemStackOld) == false) {
            if (itemStackOld != null) {
                super.getAttributeMap().removeAttributeModifiers(itemStackOld.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
            }
            if (itemStackCurrent != null) {
                super.getAttributeMap().applyAttributeModifiers(itemStackCurrent.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
            }
            this.previousItem = (itemStackCurrent == null) ? null : itemStackCurrent.copy();
        }
    }

    //region DISABLE STUFF

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public boolean canUseCommand(int var1, String var2) {
        return false;
    }

    @Override
    public boolean isSprinting() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 1.1F;
    }

    @Override
    public double getDistanceSq(double x, double y, double z) {
        return 0f;
    }

    @Override
    public double getDistance(double x, double y, double z) {
        return 0f;
    }

    @Override
    public void sendMessage(ITextComponent chatmessagecomponent) { }

    @Override
    public void sendStatusMessage(ITextComponent chatmessagecomponent, boolean type) { }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
    }

    @Override
    public void onDeath(DamageSource source) {
        return;
    }

    @Override
    public void addPotionEffect(PotionEffect p_70690_1_) {
    }

    //endregion
}