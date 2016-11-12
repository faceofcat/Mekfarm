package mekfarm.machines;

import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.FakeMekPlayer;
import mekfarm.containers.ElectricButcherContainer;
import mekfarm.items.BaseAnimalFilterItem;
import mekfarm.ui.ElectricButcherContainerGUI;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherEntity extends BaseElectricEntity<ElectricButcherContainer, ElectricButcherContainerGUI> {
    public ElectricButcherEntity() {
        super(3, 500000, 1, 6, 1, ElectricButcherContainer.class, ElectricButcherContainerGUI.class);
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if (stack == null)
            return true;

        if (slot == 0) {
            // test for weapon
            return stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND)
                    .containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName());
        }

        return false;
    }

    @Override
    protected float performWork() {
        float result = 0.0f;

        EnumFacing facing = BlocksRegistry.electricButcherBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(BlocksRegistry.electricButcherBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        //region attack animal

        ItemStack stack = this.inStackHandler.getStackInSlot(0, true);
        if ((stack != null) && (stack.stackSize > 0)) {
            // find animal
            List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, aabb);
            ItemStack filterStack = this.filtersHandler.getStackInSlot(0, true);
            BaseAnimalFilterItem filter = ((filterStack != null) && (filterStack.getItem() instanceof BaseAnimalFilterItem))
                    ? (BaseAnimalFilterItem) filterStack.getItem()
                    : null;
            EntityAnimal animalToHurt = null;
            if ((list != null) && (list.size() > 0)) {
                for (int i = 0; i < list.size(); i++) {
                    EntityAnimal thingy = list.get(i);

                    if ((animalToHurt == null) && ((filter == null) || filter.shouldHandle(thingy))) {
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

                    // TODO: damage item in input slot

                    player.setItemInUse(null);
                    result += .9f;
                }
            }
        }

        //endregion

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
}
