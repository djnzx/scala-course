package graphs.lee.colored;

import java.io.PrintStream;

public class Colored {
    private final Object msg;
    private final Attribute attr;

    public Colored(Object msg, Attribute attr) {
        this.msg = msg;
        this.attr = attr;
    }

    @Override
    public String toString() {
        return Colored.build(this.msg, this.attr);
    }

    public static String build(Object msg, Ansi.ColorFont fg) {
        return build(msg, Ansi.Style.NONE, fg, Ansi.ColorBack.NONE);
    }

    public static String build(Object msg, Ansi.Style attr, Ansi.ColorFont fg) {
        return build(msg, attr, fg, Ansi.ColorBack.NONE);
    }

    public static String build(Object msg, Attribute att) {
        return build(msg, att.style(), att.colorFont(), att.colorBack());
    }

    public static String build(Object msg, Ansi.Style attr, Ansi.ColorFont fg, Ansi.ColorBack bg) {
        return String.join("",
                new Attribute(attr, fg, bg).escapeSequence(),
                msg.toString(),
                Ansi.RESET
        );
    }

    public static void print(Object msg, Attribute att) {
        print(msg, att, System.out);
    }

    public static void print(Object msg, Attribute att, PrintStream p) {
        p.print(build(msg, att));
    }

    public static void println(Object msg, Attribute att) {
        println(msg, att, System.out);
    }

    public static void println(Object msg, Attribute att, PrintStream p) {
        p.println(build(msg, att));
    }
}
