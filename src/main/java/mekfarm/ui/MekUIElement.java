package mekfarm.ui;

/**
 * Created by CF on 2016-11-18.
 */
public abstract class MekUIElement {
    protected final MekUIContainer container;
    protected final int width;
    protected final int height;
    protected final int left;
    protected final int top;

    protected boolean isMouseOver = false;
    protected int lastLocalMouseX = 0;
    protected int lastLocalMouseY = 0;

    MekUIElement(MekUIContainer container, int left, int top, int width, int height) {
        this.container = container;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public int getLeft() { return this.left; }
    public int getTop() { return this.top; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    public void renderBackground(int mouseX, int mouseY) {}
    public void renderForeground(int mouseX, int mouseY) {}
    public void renderTopLayer(int mouseX, int mouseY) {}

    public void onMouseEnter(int localX, int localY) {
        this.isMouseOver = true;
        this.onMouseMove(localX, localY);
    }

    public void onMouseLeave() {
        this.isMouseOver = false;

        this.lastLocalMouseX = -1;
        this.lastLocalMouseY = -1;
    }

    public void onMouseMove(int localX, int localY) {
        this.lastLocalMouseX = localX;
        this.lastLocalMouseY = localY;
    }
}
