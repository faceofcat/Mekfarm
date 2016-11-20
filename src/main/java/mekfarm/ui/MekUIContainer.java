package mekfarm.ui;

import com.google.common.collect.Lists;
import mekfarm.MekfarmMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by CF on 2016-11-18.
 */
public abstract class MekUIContainer extends GuiContainer {
    protected static final ResourceLocation MACHINE_BACKGROUND = new ResourceLocation(MekfarmMod.MODID, "textures/gui/most_machines.png");

    protected TileEntity te;
    private List<MekUIElement> elements = Lists.newArrayList();
    private MekUIElement lastHoverElement = null;

    public MekUIContainer(TileEntity te, Container inventorySlotsIn) {
        super(inventorySlotsIn);

        this.te = te;
    }

    public int getGuiLeft() {
        return this.guiLeft;
    }

    public int getGuiTop() {
        return this.guiTop;
    }

    protected void addMekUIElement(MekUIElement element) {
        this.elements.add(element);
    }

    public void initGui() {
        super.initGui();
        this.elements.clear();
        this.lastHoverElement = null;
    }

    @Override
    protected final void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        this.processMouseMove(mouseX, mouseY);

        this.bindDefaultTexture();
        for (MekUIElement element : this.elements) {
            if (element != null) {
                element.renderForeground(mouseX, mouseY);
            }
        }

        this.drawGuiContainerForeground(mouseX + this.guiLeft, mouseY + this.guiTop);

        for (MekUIElement element : this.elements) {
            if (element != null) {
                element.renderTopLayer(mouseX, mouseY);
            }
        }
    }

    protected void drawGuiContainerForeground(int mouseX, int mouseY) {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        this.processMouseMove(mouseX, mouseY);

        this.bindDefaultTexture();
        for (MekUIElement element : this.elements) {
            if (element != null) {
                element.renderBackground(mouseX, mouseY);
            }
        }
    }

    private void processMouseMove(int mouseX, int mouseY) {
        if ((this.lastHoverElement != null) && this.isInside(this.lastHoverElement, mouseX, mouseY)) {
            return; // we are inside same element
        }

        if (this.lastHoverElement != null) {
            this.lastHoverElement.onMouseLeave();
            this.lastHoverElement = null;
        }

        for (MekUIElement element : this.elements) {
            if (this.isInside(element, mouseX, mouseY)) {
                this.lastHoverElement = element;
                this.lastHoverElement.onMouseEnter(mouseX - super.guiLeft, mouseY - super.guiTop);
                break;
            }
        }
    }

    private boolean isInside(MekUIElement element, int localX, int localY) {
        return (element != null)
                && (element.getLeft() <= localX)
                && (element.getTop() <= localY)
                && ((element.getLeft() + element.getWidth()) >= localX)
                && ((element.getTop() + element.getHeight()) >= localY);
    }

    public void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height) {
        super.drawTexturedModalRect(super.guiLeft + x, super.guiTop + y, textureX, textureY, width, height);
    }

    public void drawTooltip(List<String> textLines, int x, int y) {
        super.drawHoveringText(textLines, x, y);
    }

    public void bindDefaultTexture() {
        this.mc.getTextureManager().bindTexture(MACHINE_BACKGROUND);
    }

//    @Override
//    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
//        super.handleMouseClick(slotIn, slotId, mouseButton, type);
//    }
}
