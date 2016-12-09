package mekfarm.machines;

import com.google.common.collect.Multimap;
import mekfarm.MekfarmMod;
import mekfarm.common.BlockCube;
import mekfarm.common.BlockPosUtils;
import mekfarm.common.BlocksRegistry;
import mekfarm.common.FakeMekPlayer;
import mekfarm.containers.ElectricButcherContainer;
import mekfarm.items.BaseAnimalFilterItem;
import mekfarm.ui.CropClonerContainerGUI;
import mekfarm.ui.ElectricButcherContainerGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;

/**
 * Created by CF on 2016-11-11.
 */
public class ElectricButcherEntity extends BaseElectricEntity<ElectricButcherContainer> {
    public ElectricButcherEntity() {
        super(3, 500000, 1, 6, 1, ElectricButcherContainer.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getContainerGUI(IInventory playerInventory) {
        return new ElectricButcherContainerGUI(this, this.getContainer(playerInventory));
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack, boolean internal) {
        if ((stack == null) || stack.isEmpty())
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

        EnumFacing facing = BlocksRegistry.electricButcherBlock.getStateFromMeta(this.getBlockMetadata())
                .getValue(BlocksRegistry.electricButcherBlock.FACING)
                .getOpposite();
        BlockCube cube = BlockPosUtils.getCube(this.getPos(), facing, 3, 1);
        AxisAlignedBB aabb = cube.getBoundingBox();

        //region attack animal

        ItemStack stack = this.inStackHandler.getStackInSlot(0, true);
        if ((stack != null) && (stack.getCount() > 0)) {
            // find animal
            List<EntityAnimal> list = this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, aabb);
            ItemStack filterStack = this.filtersHandler.getStackInSlot(0, true);
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
                    this.inStackHandler.setStackInSlot(0, (weapon == null) ? null : weapon.copy(), true);

                    player.setItemInUse(ItemStack.EMPTY);
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
                ItemStack remaining = this.outStackHandler.distributeItems(original, false);
                if ((remaining == null) || (remaining.getCount() == 0)) {
                    this.getWorld().removeEntity(item);
                    pickedUpLoot = true;
                }
                else if (remaining.getCount() != original.getCount()) {
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
