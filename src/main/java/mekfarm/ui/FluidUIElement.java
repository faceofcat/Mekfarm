package mekfarm.ui;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by CF on 2016-11-18.
 */
public class FluidUIElement extends MekUIElement {
    private IFluidHandler fluidHandler;

    private static final int WIDTH = 18;
    private static final int HEIGHT = 54;

    public FluidUIElement(IFluidHandler fluidHandler, MekUIContainer container, int left, int top) {
        super(container, left, top, WIDTH, HEIGHT);

        this.fluidHandler = fluidHandler;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        super.container.drawTexturedRect(this.left, this.top, 1, 175, this.getWidth(), this.getHeight());

        super.container.drawTexturedRect(this.left + 2, this.top + 2, 48, 177, this.getWidth() - 4, this.getHeight() - 4);
        IFluidTankProperties[] tanks = this.fluidHandler.getTankProperties();
        if ((tanks != null) && (tanks.length > 0)) {
            IFluidTankProperties tank = tanks[0];
            FluidStack stack = tank.getContents();
            if ((stack != null) && (stack.amount > 0)) {
                int amount = (stack.amount * (this.getHeight() - 6)) / tank.getCapacity();
                if (stack.getFluid() != null) {
                    Fluid fluid = stack.getFluid();
                    int color = fluid.getColor(stack);
                    ResourceLocation still = fluid.getStill(stack); //.getStill(stack);
                    if (still != null) {
                        TextureAtlasSprite sprite = this.container.mc.getTextureMapBlocks().getTextureExtry(still.toString());
                        this.container.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                        GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
                        GlStateManager.enableBlend();
                        super.container.drawTexturedModalRect(
                                this.container.getGuiLeft() + this.left + 3,
                                this.container.getGuiTop() + this.top + 3 + this.getHeight() - 6 - amount,
                                sprite,
                                this.getWidth() - 6, amount);
                        GlStateManager.disableBlend();
                    }
                }
            }
        }
        super.container.bindDefaultTexture();
        super.container.drawTexturedRect(this.left + 2, this.top + 2, 63, 177, this.getWidth() - 4, this.getHeight() - 4);
    }

    @Override
    public void renderTopLayer(int mouseX, int mouseY) {
        if (super.isMouseOver) {
            IFluidTankProperties[] tanks = this.fluidHandler.getTankProperties();
            if ((tanks != null) && (tanks.length > 0)) {
                IFluidTankProperties tank = tanks[0];
                FluidStack stack = tank.getContents();
                int mb = (stack == null) ? 0 : stack.amount;

                List<String> lines = Lists.newArrayList();
                lines.add(String.format("%s%,d mB %sof", ChatFormatting.BLUE, mb, ChatFormatting.DARK_GRAY));
                lines.add(String.format("%s%,d mB", ChatFormatting.RESET, tank.getCapacity()));
                super.container.drawTooltip(lines, mouseX, mouseY);
            }
        }
    }
}