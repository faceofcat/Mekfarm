package mekfarm.capabilities;

import java.awt.*;

/**
 * Created by CF on 2016-11-26.
 */
public class ColoredTextLine {
    public String text = null;
    public Color color = null;
    public Color background = null;
    public Color border = null;

    public float percent = 0.0f;
    public Color percentColor = null;

    public TextAlignment alignment = TextAlignment.LEFT;

    public ColoredTextLine(String text) {
        this(null, text);
    }

    public ColoredTextLine(Color color, String text) {
        this(color, null, text);
    }

    public ColoredTextLine(Color color, Color background, String text) {
        this(color, background, null, text);
    }

    public ColoredTextLine(Color color, Color background, Color border, String text) {
        this.text = text;
        this.color = color;
        this.background = background;
        this.border = border;
    }

    public ColoredTextLine setTextAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public ColoredTextLine setProgress(float percent, Color percentColor) {
        this.percent = percent;
        this.percentColor = percentColor;
        return this;
    }

    public enum TextAlignment {
        LEFT, CENTER, RIGHT
    }
}
