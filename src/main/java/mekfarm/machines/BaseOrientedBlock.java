package mekfarm.machines;

import mekfarm.MekfarmMod;
import net.minecraft.block.material.Material;
import net.ndrei.teslacorelib.blocks.OrientedBlock;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

/**
 * Created by CF on 2016-11-02.
 */
public abstract class BaseOrientedBlock<T extends SidedTileEntity> extends OrientedBlock {
    protected BaseOrientedBlock(String blockId, Class<T> teClass) {
        this(blockId, teClass, Material.ROCK);
    }

    protected BaseOrientedBlock(String blockId, Class<T> teClass, Material material) {
        super(MekfarmMod.MODID, MekfarmMod.creativeTab, blockId, teClass, material);
    }
}
