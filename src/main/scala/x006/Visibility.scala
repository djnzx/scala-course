package x006

object Visibility extends App {

  class Printer {
    def print = println(Printer.value)
  }

  object Printer {
    private val value = "Hi"
  }

  val p = new Printer
  p.print
  echo(MAGIC)
  Margin.Bottom

  println(Planet.values.filter(_.radius > 7.0e6))

}
