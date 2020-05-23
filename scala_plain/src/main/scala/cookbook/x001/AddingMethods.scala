package cookbook.x001

object AddingMethods extends App {

  implicit class StringXX(s: String) {
    def increment = s.map((c: Char) => (c + 1).toChar)
    def decrement = s.map((c: Char) => (c - 1).toChar)
    def hidden = s.replaceAll(".", "*")
  }

  println("HAL".increment)
  println("HAL".decrement)
  println("Hello".hidden)
}
