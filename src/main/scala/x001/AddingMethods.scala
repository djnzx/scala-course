package x001

object AddingMethods extends App {
  implicit class StringXX(s: String) {
    def increment = s.map(c => (c+1).toChar)
  }

  println("HAL".increment)
}
