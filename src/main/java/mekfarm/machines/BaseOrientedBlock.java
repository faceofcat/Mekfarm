package mekfarm.machines;

import mekfarm.MekfarmMod;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ndrei.teslacorelib.block.OrientedBlock;

/**
 * Created by CF on 2016-11-02.
 */
public abstract class BaseOrientedBlock<T extends BaseElectricEntity> extends OrientedBlock implements ITileEntityProvider {
    private int guiId;

    protected BaseOrientedBlock(String blockId, Class<T> teClass, int guiId) {
        this(blockId, teClass, guiId, Material.ROCK);
    }

    protected BaseOrientedBlock(String blockId, Class<T> teClass, int guiId, Material material) {
        super(blockId, teClass, material);
        this.guiId = guiId;

        this.setUnlocalizedName(MekfarmMod.MODID + "." + this.getRegistryName().getResourcePath());
        this.setCreativeTab(MekfarmMod.creativeTab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) {
            return true;
        }

        // Only execute on the server
        if (!world.isRemote && (this.guiId > 0)) {
            player.openGui(MekfarmMod.instance, this.guiId, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }
}
