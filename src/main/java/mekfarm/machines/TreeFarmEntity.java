package mekfarm.machines;

import com.google.common.collect.Lists;
import mekfarm.common.BlockCube;
import mekfarm.machines.wrappers.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemHandlerHelper;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CF on 2016-10-28.
 */
public class TreeFarmEntity extends ElectricMekfarmMachine {
    private static ArrayList<Item> acceptedItems = new ArrayList<>();
    private TreeScanner scanner = new TreeScanner();

    static {
        TreeFarmEntity.acceptedItems.add(Items.SHEARS);
        TreeFarmEntity.acceptedItems.add(Item.getItemFromBlock(Blocks.SAPLING));
    }

    public TreeFarmEntity() {
        super(TreeFarmEntity.class.getName().hashCode());
    }

    @Override
    protected boolean acceptsInputStack(int slot, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack))
            return true;

        return TreeFarmEntity.acceptedItems.contains(stack.getItem());
    }

    private static final float SCAN_PERCENT = 0.025f;
    private static final float BREAK_PERCENT = 0.05f;
    private static final float PLANT_PERCENT = 0.10f;

    @Override
    protected float performWork() {
        EnumFacing facing = super.getFacing();
        float result = 0;
        BlockCube cube = this.getWorkArea(facing.getOpposite(), 1);

        //#region scan trees

        result += this.scanner.scan(this.getWorld(), cube, SCAN_PERCENT, 1.0f);
        // MekfarmMod.logger.info("Scanned " + Math.round(result / 0.025f) + " new blocks. " + this.scanner.blockCount() + " total.");

        //#endregion

        //#region plant saplings

        List<ITreeSaplingWrapper> saplings = TreeWrapperFactory.getSaplingWrappers(ItemStackUtil.getCombinedInventory(this.inStackHandler));
        if ((saplings != null) && (saplings.size() > 0)) {
            for (BlockPos pos: cube) {
                if (result > (1 - PLANT_PERCENT)) {
                    break;
                }

                if (this.getWorld().isAirBlock(pos)) {
                    for (ITreeSaplingWrapper sapling: saplings) {
                        if (sapling.canPlant(this.getWorld(), pos)) {
                            int planted = sapling.plant(this.getWorld(), pos);
                            if (planted > 0) {
                                ItemStack original = sapling.getStack();
                                int extracted = ItemStackUtil.extractFromCombinedInventory(this.inStackHandler, original, planted);
                                if (ItemStackUtil.getSize(original) <= extracted) {
                                    saplings.remove(sapling);
                                }
                                else {
                                    ItemStackUtil.shrink(original, extracted);
                                }

                                result += PLANT_PERCENT;
                                break;
                            }
                        }
                    }
                }
            }
        }

        //#endregion

        //#region cut trees

        boolean hasShears = false;
        int shearsSlot = 0;
        for(int index = 0; index < this.inStackHandler.getSlots(); index++) {
            ItemStack stack = this.inStackHandler.getStackInSlot(index);
            if (!ItemStackUtil.isEmpty(stack) && (stack.getItem() == Items.SHEARS)) {
                hasShears = true;
                shearsSlot = index;
                break;
            }
        }

        List<ItemStack> items = Lists.newArrayList();
        while ((result <= (1 - BREAK_PERCENT)) && (this.scanner.blockCount() > 0)) {
            BlockPos pos = this.scanner.popScannedPos();
            if (pos != null) {
                ITreeBlockWrapper wrapper = TreeWrapperFactory.getBlockWrapper(this.getWorld(), pos, null);
                if (wrapper instanceof ITreeLeafWrapper) {
                   if (hasShears) {
                       items.addAll(((ITreeLeafWrapper) wrapper).shearBlock());
                       if (shearsSlot >= 0) {
                           if (this.inStackHandler.getStackInSlot(shearsSlot).attemptDamageItem(1, this.getWorld().rand)) {
                               this.inStackHandler.setStackInSlot(shearsSlot, ItemStackUtil.getEmptyStack());
                               shearsSlot = -1;
                               hasShears = false;
                           }
                       }
                   }
                   else {
                       items.addAll(wrapper.breakBlock(1));
                   }
                }
                else if (wrapper != null) {
                    items.addAll(wrapper.breakBlock(1));
                }

                result += BREAK_PERCENT;
            }
        }

        if (items.size() > 0) {
            for (ItemStack stack : items) {
                ItemStack remaining = ItemStackUtil.insertItemInExistingStacks(this.inStackHandler, stack, false);
                if (!ItemStackUtil.isEmpty(remaining)) {
                    remaining = ItemHandlerHelper.insertItem(this.outStackHandler, stack, false);
                }
                if (!ItemStackUtil.isEmpty(remaining)) {
//                    BlockPos spawnPos = this.pos.offset(facing);
//                    world.spawnEntity(new EntityItem(this.getWorld(), spawnPos.getX() + .5, spawnPos.getY() + .5, spawnPos.getZ() + .5, remaining));
                    super.spawnItemFromFrontSide(remaining);
                }
            }
        }

        //#endregion

        return result;
    }
}
