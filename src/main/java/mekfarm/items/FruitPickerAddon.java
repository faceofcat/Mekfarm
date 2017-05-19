package mekfarm.items;

import mekfarm.MekfarmMod;
import mekfarm.common.IAdditionalProcessingAddon;
import mekfarm.machines.CropFarmEntity;
import mekfarm.machines.ElectricMekfarmMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;
import net.ndrei.teslacorelib.items.BaseAddon;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by CF on 2017-05-19.
 */
public class FruitPickerAddon extends BaseAddon implements IAdditionalProcessingAddon {
    public FruitPickerAddon() {
        super(MekfarmMod.MODID, MekfarmMod.creativeTab, "fruit_picker_addon");
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(this,
                " t ", "tat", "shs",
                't', "treeSapling",
                'a', TeslaCoreLib.baseAddon,
                's', "stickWood",
                'h', Blocks.HOPPER);
    }

    @Override
    public boolean canBeAddedTo(SidedTileEntity machine) {
        return (machine instanceof CropFarmEntity);
    }

    private static final float PICK_ENERGY = .05f;

    @Override
    public float processAddon(ElectricMekfarmMachine machine, float availableProcessing) {
        float energyUsed = 0.0f;
        if (availableProcessing >= PICK_ENERGY) {
            for(BlockPos pos: machine.getGroundArea()) {
                for(int y = 5; y >= 1; y--) {
                    BlockPos current = pos.up(y);
                    if (!machine.getWorld().isAirBlock(current)) {
                        IBlockState state = machine.getWorld().getBlockState(current);
                        Block block = state.getBlock();
                        try {
                            Method isMature = block.getClass().getMethod("isMature", IBlockState.class);
                            Boolean mature = (Boolean)isMature.invoke(block, state);
                            if (mature) {
                                List<ItemStack> loot = block.getDrops(machine.getWorld(), current, state, 1);
                                if (machine.outputItems(loot)) {
                                    machine.getWorld().setBlockState(current, block.getDefaultState());
                                }
                            }
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            // ignore it
                        }
                    }
                }
            }
        }
        return energyUsed;
    }
}
