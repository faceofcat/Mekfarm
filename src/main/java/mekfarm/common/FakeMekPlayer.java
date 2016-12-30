package mekfarm.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.UUID;

/**
 * Created by CF on 2016-11-12.
 *
 * Inspired from: CoFHCore/src/main/java/cofh/core/entity/CoFHFakePlayer.java
 * not using FakePlayer as base class because of 'roots' mod and someone asking for me to allow automating that.
 * TODO: figure out if not using FakePlayer is a bad idea in general
 */
public class FakeMekPlayer extends EntityPlayerMP {
    public ItemStack previousItem = ItemStackUtil.getEmptyStack();

    public FakeMekPlayer(WorldServer world, GameProfile name)
    {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, name, new PlayerInteractionManager(world));
        this.addedToChunk = false;
        this.onGround = true;
    }

    public FakeMekPlayer(WorldServer world) {
        this(world, new GameProfile(UUID.randomUUID(), "[Mekfarm Fake Player]"));
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

        if (!ItemStack.areItemStacksEqual(itemStackCurrent, itemStackOld)) {
            if (!ItemStackUtil.isEmpty(itemStackOld)) {
                super.getAttributeMap().removeAttributeModifiers(itemStackOld.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
            }
            if (!ItemStackUtil.isEmpty(itemStackCurrent)) {
                super.getAttributeMap().applyAttributeModifiers(itemStackCurrent.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
            }
            this.previousItem = ItemStackUtil.isEmpty(itemStackCurrent) ? ItemStackUtil.getEmptyStack() : itemStackCurrent.copy();
        }
    }

    //region DISABLE STUFF

    @Override public Vec3d getPositionVector(){ return new Vec3d(0, 0, 0); }
    @Override public boolean isEntityInvulnerable(DamageSource source){ return true; }
    @Override public boolean canAttackPlayer(EntityPlayer player){ return false; }
    @Override public Entity changeDimension(int dim){ return this; }
    @Override public void handleClientSettings(CPacketClientSettings pkt){ return; }

    @Override public boolean isSneaking() { return false; }
    @Override public boolean canUseCommand(int var1, String var2) { return false; }
    @Override public boolean isSprinting() { return false; }
    @Override public float getEyeHeight() { return 1.1F; }
    @Override public double getDistanceSq(double x, double y, double z) { return 0f; }
    @Override public double getDistance(double x, double y, double z) { return 0f; }
    @Override public void sendMessage(ITextComponent chatmessagecomponent) { }
    @Override public void sendStatusMessage(ITextComponent chatmessagecomponent, boolean bp) { }
    @Override public void addStat(StatBase par1StatBase, int par2) { }
    @Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) { }
    @Override public void onDeath(DamageSource source) { return; }
    @Override public void addPotionEffect(PotionEffect p_70690_1_) { }

    //endregion
}