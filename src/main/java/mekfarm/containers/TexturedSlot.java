package mekfarm.containers;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by CF on 2016-11-12.
 */
public class TexturedSlot extends InternalSlot {
    private static final int DRAW_ON_TICK = 100;

    public int textureX;
    public int textureY;
    public int textureCount;

    private int tick;
    private int textureIndex = 0;

    public TexturedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, int textureX, int textureY, int textureCount) {
        super(itemHandler, index, xPosition, yPosition);

        this.textureX = textureX;
        this.textureY = textureY;
        this.textureCount = textureCount;

        this.tick = (42 * index) % DRAW_ON_TICK;
        this.textureIndex = index % this.textureCount;
    }

    public void draw(int guiLeft, int guiTop, GuiContainer gui) {
        if (this.tick++ > DRAW_ON_TICK) {
            this.tick = 0;
            this.textureIndex = (this.textureIndex + 1) % this.textureCount;
        }

        if (this.getHasStack() == false) {
            gui.drawTexturedModalRect(
                    guiLeft + this.xDisplayPosition - 1, guiTop + this.yDisplayPosition - 1,
                    this.textureX + textureIndex * 18, this.textureY,
                    18, 18);
        }
    }
}
