package mekfarm.machines.wrappers.animals;

import com.google.common.collect.Lists;
import mekfarm.machines.wrappers.IAnimalWrapper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IShearable;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.List;

/**
 * Created by CF on 2016-12-10.
 */
public class VanillaGenericAnimal implements IAnimalWrapper {
    private EntityAnimal animal;

    public VanillaGenericAnimal(EntityAnimal animal) {
        this.animal = animal;
    }

    @Override
    public EntityAnimal getAnimal() {
        return this.animal;
    }

    @Override
    public boolean breedable() {
        EntityAnimal animal = this.getAnimal();
        return ((animal != null) && !animal.isInLove() && !animal.isChild() && (animal.getGrowingAge() == 0));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return this.getAnimal().isBreedingItem(stack);
    }

    @Override
    public boolean canMateWith(IAnimalWrapper wrapper) {
        return this.breedable() && wrapper.breedable() && (this.getAnimal().getClass() == wrapper.getAnimal().getClass());
    }

    @Override
    public int mate(EntityPlayer player, ItemStack stack, IAnimalWrapper wrapper) {
        int consumedFood;
        int neededFood = 2 * this.getFoodNeededForMating(stack);
        if (ItemStackUtil.getSize(stack) < neededFood) {
            consumedFood = 0;
        } else if (!this.canMateWith(wrapper) || !(this.isFood(stack))) {
            consumedFood = 0;
        } else {
            this.getAnimal().setInLove(player);
            wrapper.getAnimal().setInLove(player);
            consumedFood = neededFood;
        }

        return consumedFood;
    }

    protected int getFoodNeededForMating(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean shearable() {
        return !(this.getAnimal() instanceof EntityMooshroom) && (this.getAnimal() instanceof IShearable);
    }

    @Override
    public boolean canBeShearedWith(ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack) || (stack.getItem() != Items.SHEARS)) {
            return false;
        }
        boolean isShearable = false;
        EntityAnimal animal = this.getAnimal();
        if (this.shearable() && (animal instanceof IShearable)) {
            IShearable shearable = (IShearable)animal;
            isShearable = shearable.isShearable(stack, animal.getEntityWorld(), animal.getPosition());
        }
        return isShearable;
    }

    @Override
    public List<ItemStack> shear(ItemStack stack, int fortune) {
        List<ItemStack> result = Lists.newArrayList();
        EntityAnimal animal = this.getAnimal();
        if ((animal instanceof IShearable)) {
            IShearable shearable = (IShearable)animal;
            if (shearable.isShearable(stack, animal.getEntityWorld(), animal.getPosition())) {
                result = shearable.onSheared(stack, animal.getEntityWorld(), animal.getPosition(), fortune);
            }
        }
        return result;
    }

    @Override
    public boolean canBeMilked() {
        EntityAnimal animal = this.getAnimal();
        return (animal instanceof EntityCow) && !animal.isChild();
    }

    @Override
    public ItemStack milk() {
        return this.canBeMilked()
                ? new ItemStack(Items.MILK_BUCKET, 1)
                : ItemStackUtil.getEmptyStack();
    }

    @Override
    public boolean canBeBowled() {
        EntityAnimal animal = this.getAnimal();
        return (animal instanceof EntityMooshroom) && !animal.isChild();
    }

    @Override
    public ItemStack bowl() {
        return this.canBeBowled()
                ? new ItemStack(Items.MUSHROOM_STEW, 1)
                : ItemStackUtil.getEmptyStack();
    }

    public static void populateFoodItems(List<Item> food) {
        // cows / mooshrooms
        food.add(Items.WHEAT);

        // chicken
        food.add(Items.WHEAT_SEEDS);
        food.add(Items.BEETROOT_SEEDS);
        food.add(Items.PUMPKIN_SEEDS);
        food.add(Items.MELON_SEEDS);

        // pigs
        food.add(Items.CARROT);
        food.add(Items.POTATO);
        food.add(Items.BEETROOT);

        food.add(Items.GOLDEN_CARROT);
        food.add(Item.getItemFromBlock(Blocks.HAY_BLOCK));
        food.add(Items.APPLE);
    }
}
