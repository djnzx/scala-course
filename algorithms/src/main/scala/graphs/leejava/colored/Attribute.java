package graphs.leejava.colored;

import java.util.ArrayList;
import java.util.Arrays;

public class Attribute {
    private final Ansi.Style style;
    private final Ansi.ColorFont colorFont;
    private final Ansi.ColorBack colorBack;

    public static final Attribute CLEAR = new Attribute();
    public static final Attribute RED = new Attribute(Ansi.ColorFont.RED);
    public static final Attribute GREEN = new Attribute(Ansi.ColorFont.GREEN);
    public static final Attribute BLUE = new Attribute(Ansi.ColorFont.BLUE);
    public static final Attribute YELLOW = new Attribute(Ansi.ColorFont.YELLOW);
    public static final Attribute CYAN = new Attribute(Ansi.ColorFont.CYAN);
    public static final Attribute MAGENTA = new Attribute(Ansi.ColorFont.MAGENTA);

    public Attribute() {
        this(Ansi.Style.NONE, Ansi.ColorFont.NONE, Ansi.ColorBack.NONE);
    }

    public Attribute(Ansi.ColorFont fg) {
        this(Ansi.Style.NONE, fg, Ansi.ColorBack.NONE);
    }

    public Attribute(Ansi.ColorBack bg) {
        this(Ansi.Style.NONE, Ansi.ColorFont.NONE, bg);
    }

    public Attribute(Ansi.Style st) {
        this(st, Ansi.ColorFont.NONE, Ansi.ColorBack.NONE);
    }

    public Attribute(Ansi.Style st, Ansi.ColorFont fg, Ansi.ColorBack bg) {
        this.style = st;
        this.colorFont = fg;
        this.colorBack = bg;
    }

    public Ansi.Style style() {
        return style;
    }

    public Ansi.ColorFont colorFont() {
        return colorFont;
    }

    public Ansi.ColorBack colorBack() {
        return colorBack;
    }

    public Attribute withColor(Ansi.ColorFont color) {
        return new Attribute(this.style, color, this.colorBack);
    }

    public Attribute withBackground(Ansi.ColorBack color) {
        return new Attribute(this.style, this.colorFont, color);
    }

    public Attribute withStyle(Ansi.Style attr) {
        return new Attribute(attr, this.colorFont, this.colorBack);
    }

    public Attribute bold() {
        return withStyle(Ansi.Style.BOLD);
    }

    public Attribute reverse() {
        return withStyle(Ansi.Style.REVERSE);
    }

    public Attribute underline() {
        return withStyle(Ansi.Style.UNDERLINE);
    }

    public String escapeSequence() {
        ArrayList<String> attributes = new ArrayList<>();

        if (colorFont != Ansi.ColorFont.NONE) {
            attributes.add(colorFont.code());
        }
        if (colorBack != Ansi.ColorBack.NONE) {
            attributes.add(colorBack.code());
        }
        if (style != Ansi.Style.NONE) {
            attributes.add(style.code());
        }

        return attributes.isEmpty() ? Ansi.RESET :
                Ansi.PREFIX + String.join(Ansi.SEPARATOR, attributes)+ Ansi.POSTFIX;
    }

    public String escapeasList() {
        return Arrays.toString(escapeSequence().getBytes());
    }

    @Override
    public String toString() {
        return String.format("Attribute:{style=%s, colorFont=%s, colorBack=%s}", style, colorFont, colorBack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (style != attribute.style) return false;
        if (colorFont != attribute.colorFont) return false;
        return colorBack == attribute.colorBack;
    }

    @Override
    public int hashCode() {
        int result = style != null ? style.hashCode() : 0;
        result = 31 * result + (colorFont != null ? colorFont.hashCode() : 0);
        result = 31 * result + (colorBack != null ? colorBack.hashCode() : 0);
        return result;
    }
}
