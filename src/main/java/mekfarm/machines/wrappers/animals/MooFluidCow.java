package mekfarm.machines.wrappers.animals;

import mekfarm.MekfarmMod;
import mekfarm.common.FakeMekPlayer;
import mekfarm.machines.wrappers.IAnimalWrapper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.Constants;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.List;

/**
 * Created by CF on 2017-05-10.
 */
public class MooFluidCow implements IAnimalWrapper {
    private EntityCow cow;

    public MooFluidCow(EntityCow cow) {
        this.cow = cow;
    }

    @Override
    public EntityAnimal getAnimal() {
        return this.cow;
    }

    //#region unsupported methods

    @Override
    public int mate(EntityPlayer player, ItemStack stack, IAnimalWrapper wrapper) {
        return 0;
    }

    @Override
    public boolean shearable() { return false; }

    @Override
    public boolean canBeShearedWith(ItemStack stack) { return false; }

    @Override
    public List<ItemStack> shear(ItemStack stack, int fortune) { return null; }

    @Override
    public boolean canBeBowled() { return false; }

    @Override
    public ItemStack bowl() { return null; }

    //#endregion

    //#region breeding

    @Override
    public boolean breedable() {
        return true;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return this.cow.isBreedingItem(stack);
    }

    @Override
    public boolean canMateWith(IAnimalWrapper wrapper) {
        return this.cow.canMateWith(wrapper.getAnimal());
    }

    //#endregion

    //#region "milking"

    @Override
    public boolean canBeMilked() {
//        try {
//            Method m = this.cow.getClass().getMethod("getNextUseCooldown");
//            int cooldown = (int)m.invoke(this.cow);
//            if (cooldown == 0) {
//                return true;
//            }
//            else {
//                MekfarmMod.logger.info(cooldown);
//            }
//        }
//        catch (Throwable t) {
//            MekfarmMod.logger.warn(t);
//            return false;
//        }
        NBTTagCompound nbt = this.cow.serializeNBT();
        if (nbt.hasKey("NextUseCooldown", Constants.NBT.TAG_INT)) {
            // MekfarmMod.logger.info("cooldown: " + nbt.getInteger("NextUseCooldown"));
            return (nbt.getInteger("NextUseCooldown") == 0);
        }
        else if (nbt.hasKey("CurrentUseCooldown", Constants.NBT.TAG_INT)) {
            // MekfarmMod.logger.info("cooldown: " + nbt.getInteger("CurrentUseCooldown"));
            return (nbt.getInteger("CurrentUseCooldown") == 0);
        }
        else {
            // MekfarmMod.logger.info("no cooldown info.");
        }
        return false;
    }

    @Override
    public ItemStack milk() {
        FakeMekPlayer player = MekfarmMod.getFakePlayer(this.cow.getEntityWorld());
        player.setActiveHand(EnumHand.MAIN_HAND);
        player.setItemInHand(new ItemStack(Items.BUCKET));
        if (this.cow.processInteract(player, EnumHand.MAIN_HAND)) {
            return player.getHeldItemMainhand();
        }
        return ItemStackUtil.getEmptyStack();
    }

    //#endregion
}
