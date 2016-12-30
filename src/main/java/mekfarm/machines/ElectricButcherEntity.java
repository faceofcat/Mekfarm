package mekfarm.machines;

import com.google.common.collect.Multimap;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.FakeMekPlayer;
import mekfarm.items.BaseAnimalFilterItem;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemHandlerHelper;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.Collection;
import java.util.List;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherEntity extends ElectricMekfarmMachine {
    public ElectricButcherEntity() {
        super(3);
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public GuiContainer getContainerGUI(IInventory playerInventory) {
//        return new ElectricButcherContainerGUI(this, this.getContainer(playerInventory));
//    }

    @Override
    protected int getInputSlots() {
        return 1;
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack))
            return true;

        if (slot == 0) {
            // test for weapon
            Multimap<String, AttributeModifier> map = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            if (map.containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                Collection<AttributeModifier> modifiers = map.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
                for(AttributeModifier modifier : modifiers) {
                    if (modifier.getAmount() > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected float performWork() {
        float result = 0.0f;

        EnumFacing facing = super.getFacing();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing.getOpposite(), 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        //region attack animal

        ItemStack stack = this.inStackHandler.getStackInSlot(0);
        if (!ItemStackUtil.isEmpty(stack)) {
            // find animal
            List<EntityAnimal> list = this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, aabb);
            ItemStack filterStack = this.addonItems.getStackInSlot(0);
            BaseAnimalFilterItem filter = ((filterStack != null) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                    ? (BaseAnimalFilterItem) filterStack.getItem()
                    : null;
            EntityAnimal animalToHurt = null;
            if ((list != null) && (list.size() > 0)) {
                for (int i = 0; i < list.size(); i++) {
                    EntityAnimal thingy = list.get(i);

                    if ((animalToHurt == null) && ((filter == null) || filter.canProcess(this, i, thingy))) {
                        animalToHurt = thingy;
                        break;
                    }
                }
            }
            if (animalToHurt != null) {
                FakeMekPlayer player = MekfarmMod.getFakePlayer(this.getWorld());
                if (player != null) {
                    player.setItemInUse(stack.copy());
                    player.attackTargetEntityWithCurrentItem(animalToHurt);

                    ItemStack weapon = player.getHeldItem(EnumHand.MAIN_HAND);
                    this.inStackHandler.setStackInSlot(0, ItemStackUtil.isEmpty(weapon) ? ItemStackUtil.getEmptyStack() : weapon.copy());

                    player.setItemInUse(ItemStackUtil.getEmptyStack());
                    result += .9f;
                }
            }
        }

        //endregion

        //region collect loot

        List<EntityItem> items = this.getWorld().getEntitiesWithinAABB(EntityItem.class, aabb);
        boolean pickedUpLoot = false;
        if (!items.isEmpty()) {
            for (EntityItem item: items) {
                ItemStack original = item.getEntityItem();
                ItemStack remaining = ItemHandlerHelper.insertItem(this.outStackHandler, original, false);
                if (ItemStackUtil.isEmpty(remaining)) {
                    this.getWorld().removeEntity(item);
                    pickedUpLoot = true;
                }
                else if (ItemStackUtil.getSize(remaining) != ItemStackUtil.getSize(original)) {
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
}
