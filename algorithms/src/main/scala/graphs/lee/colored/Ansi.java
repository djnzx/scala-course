package graphs.lee.colored;

/**
 * @author alexr
 * @version 0.1
 * @see <a href="http://ascii-table.com/ansi-escape-sequences.php">Ansi escape
 * codes</a>
 */
public class Ansi {

    //public static final String PREFIX = "\033[";
    public static final String PREFIX = "\u001B[";
    public static final String SEPARATOR = ";";
    public static final String POSTFIX = "m";
    public static final String RESET = PREFIX + "0" + POSTFIX;

    public enum ColorFont {
        BLACK("30"),
        RED("31"),
        GREEN("32"),
        YELLOW("33"),
        BLUE("34"),
        MAGENTA("35"),
        CYAN("36"),
        WHITE("37"),
        NONE("");

        private final String _code;

        ColorFont(String code) {
            _code = code;
        }

        public String code() {
            return _code;
        }
    }

    public enum ColorBack {
        BLACK("40"),
        RED("41"),
        GREEN("42"),
        YELLOW("43"),
        BLUE("44"),
        MAGENTA("45"),
        CYAN("46"),
        WHITE("47"),
        NONE("");

        private final String _code;

        ColorBack(String code) {
            _code = code;
        }

        public String code() {
            return _code;
        }
    }

    public enum Style {
        CLEAR("0"),
        BOLD("1"),
        LIGHT("1"),
        DARK("2"),
        UNDERLINE("4"),
        REVERSE("7"),
        HIDDEN("8"),
        NONE("");

        private final String _code; // Ansi escape code

        Style(String code) {
            _code = code;
        }

        public String code() {
            return _code;
        }
    }
}