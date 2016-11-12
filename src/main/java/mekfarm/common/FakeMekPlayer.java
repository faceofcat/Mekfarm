package mekfarm.common;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
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

    public void setItemInHand(ItemStack m_item) {
        this.inventory.currentItem = 0;
        this.inventory.setInventorySlotContents(0, m_item);
    }

    public void setItemInHand(int slot) {
        this.inventory.currentItem = slot;
    }

    public void setItemInUse(ItemStack heldItemMainhand, int i) {
        this.ticksSinceLastSwing = (int)this.getCooldownPeriod() + 1;
    }

    @Override
    public void onUpdate() {
        ItemStack itemStackOld = this.previousItem;
        ItemStack itemStackCurrent = getHeldItemMainhand();

        if (!ItemStack.areItemStacksEqual(itemStackCurrent, itemStackOld)) {
            if (itemStackOld != null) {
                getAttributeMap().removeAttributeModifiers(itemStackOld.getAttributeModifiers(null));
            }
            if (itemStackCurrent != null) {
                getAttributeMap().applyAttributeModifiers(itemStackCurrent.getAttributeModifiers(null));
            }
            // this.myName = "[MEK]" + (itemStackCurrent != null ? " using " + itemStackCurrent.getDisplayName() : "");
        }
        this.previousItem = itemStackCurrent == null ? null : itemStackCurrent.copy();
    }

    //region DISABLE STUFF

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public boolean canCommandSenderUseCommand(int var1, String var2) {
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
    public void addChatMessage(ITextComponent chatmessagecomponent) {
    }

    @Override
    public void addChatComponentMessage(ITextComponent chatmessagecomponent) {
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
    }

	public boolean isEntityInvulnerable() {
		return true;
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