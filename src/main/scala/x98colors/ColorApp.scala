package x98colors

object ColorApp extends App {
  import Console._

  implicit class StringColor(s: String) {
    def reset = s"$s$RESET"
    def red = s"$RED$s$RESET"
    def blue = s"$BLUE$s$RESET"
  }

  println("Hello".red)
  println("Hello")
  println("Hello".blue)
}
