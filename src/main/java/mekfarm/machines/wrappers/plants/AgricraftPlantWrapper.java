package mekfarm.machines.wrappers.plants;

import mekfarm.MekfarmMod;
import mekfarm.common.FakeMekPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

import java.util.List;

/**
 * Created by CF on 2017-05-14.
 */
public class AgricraftPlantWrapper extends VanillaGenericPlant {
    public AgricraftPlantWrapper(Block block, IBlockState state, World world, BlockPos pos) {
        super(block, state, world, pos);
    }

    @Override
    public List<ItemStack> harvest(int fortune) {
        FakeMekPlayer player = MekfarmMod.getFakePlayer(super.world);
        player.setItemInHand(ItemStackUtil.getEmptyStack());
        super.block.onBlockActivated(super.world, super.pos, super.state, player, EnumHand.OFF_HAND,
                EnumFacing.UP, .5f, .5f, .5f);

        return this.harvestDrops();
    }

    @Override
    public int useFertilizer(ItemStack fertilizer) {
        FakeMekPlayer player = MekfarmMod.getFakePlayer(super.world);
        ItemStack stack = fertilizer.copy();
        player.setItemInUse(stack);
        return super.block.onBlockActivated(super.world, super.pos, super.state, player, EnumHand.OFF_HAND,
                EnumFacing.UP, .5f, .5f, .5f
        ) ? 1 : 0;
    }
}
