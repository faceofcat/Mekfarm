package mekfarm.containers;

import mekfarm.MekfarmMod;
import mekfarm.entities.FarmTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by CF on 2016-10-28.
 */
public class FarmContainerGUI extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(MekfarmMod.MODID, "textures/gui/testcontainer.png");

    public FarmContainerGUI(FarmTileEntity tileEntity, FarmContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
