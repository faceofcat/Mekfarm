package mekfarm.containers;

import mekfarm.common.IContainerProvider;
import mekfarm.farms.AnimalFarmEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by CF on 2016-10-28.
 */
public class MekfarmGuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IContainerProvider) {
            return ((IContainerProvider)te).getContainer(player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IContainerProvider) {
            return ((IContainerProvider)te).getContainerGUI(player.inventory);
        }
//        if (te instanceof AnimalFarmEntity) {
//            AnimalFarmEntity containerTileEntity = (AnimalFarmEntity) te;
//            return new FarmContainerGUI(containerTileEntity, new FarmContainer(player.inventory, containerTileEntity));
//        }
        return null;
    }
}