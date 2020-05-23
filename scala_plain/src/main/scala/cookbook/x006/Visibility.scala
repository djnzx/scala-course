package cookbook.x006

object Visibility extends App {

  class Printer {
    def print: Unit = println(Printer.value)
  }

  object Printer {
    private val value = "Hi"
  }

  val p = new Printer
  p.print

  echo(MAGIC) // package object

  val mar: Margin.Value = Margin.Bottom

  println(Planet.values.filter(_.radius > 7.0e6))

}
