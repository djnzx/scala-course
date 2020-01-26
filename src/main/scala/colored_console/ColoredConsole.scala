package colored_console

object ColoredConsole {
  import Console._

  implicit class StringColor(s: String) {
    def unary_+ = s"$GREEN$s$RESET"
    def reset = s"$s$RESET"
    def red = s"$RED$s$RESET"
    def blue = s"$BLUE$s$RESET"
  }

}
