package mekfarm.client;

import mekfarm.MekfarmMod;
import mekfarm.machines.AnimalGymEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.ndrei.teslacorelib.compatibility.FontRendererUtil;
import net.ndrei.teslacorelib.gui.BasicRenderedGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;

/**
 * Created by CF on 2017-02-09.
 */
public class AnimalGymInfoPiece extends BasicRenderedGuiPiece {
    private AnimalGymEntity entity;

    public AnimalGymInfoPiece(AnimalGymEntity entity, int left, int top) {
        super(left, top, 54, 54, MekfarmMod.MACHINES_BACKGROUND, 1, 1);

        this.entity = entity;
    }

    @Override
    public void drawForegroundLayer(BasicTeslaGuiContainer container, int guiX, int guiY, int mouseX, int mouseY) {
        FontRenderer font = FontRendererUtil.getFontRenderer();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.getLeft() + 2, this.getTop() + 2, 0);
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.65, 0.65, 1);
        font.drawString("" + TextFormatting.BOLD + TextFormatting.DARK_PURPLE + this.entity.getCurrentAnimalType(),
                4, 4, 0xFFFFFF);
        font.drawString("" + TextFormatting.DARK_BLUE + String.format("%.2f", this.entity.getPowerPerTick()) + " T / tick",
                0, 2 * (font.FONT_HEIGHT + 2), 0xFFFFFF);
        font.drawString("" + TextFormatting.DARK_AQUA + this.entity.getMaxPowerForCurrent() + " T" + TextFormatting.RESET,
                0, 3 * (font.FONT_HEIGHT + 2), 0xFFFFFF);
        GlStateManager.popMatrix();

        GlStateManager.scale(0.5, 0.5, 1);
        EntityAnimal a = this.entity.getCurrentAnimal();
        if (a != null) {
            container.mc.getTextureManager().bindTexture(Gui.ICONS);
            float f = a.getMaxHealth();
            int i = MathHelper.ceil(a.getHealth());
            int j1 = 80;

            for (int j5 = MathHelper.ceil(f / 2.0F) - 1; j5 >= 0; --j5) {
                int textureX = 16;

                int x = j5 % 10 * 10;
                int y = j1 - (j5 / 10 - 1) * 10;

                container.drawTexturedModalRect(x, y, 16, 0, 9, 9);

                if (j5 * 2 + 1 < i) {
                    container.drawTexturedModalRect(x, y, textureX + 36, 0, 9, 9);
                }

                if (j5 * 2 + 1 == i) {
                    container.drawTexturedModalRect(x, y, textureX + 45, 0, 9, 9);
                }
            }
        }
        GlStateManager.popMatrix();
    }
}
