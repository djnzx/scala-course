package aa_fp

object Fps068 extends App {

  case class Debuggable(value: Int, message: String) {
    def map(f: Int => Int): Debuggable = Debuggable(f(value), message)
    def flatMap(f: Int => Debuggable): Debuggable =
      f(value) match { case Debuggable(i,s) => Debuggable(i, s"$message $s") }
  }

  def f(x: Int): Debuggable = Debuggable(x + 2, s"f($x)=${x+2}")
  def g(x: Int): Debuggable = Debuggable(x * 2, s"g($x)=${x*2}")
  def h(x: Int): Debuggable = Debuggable(x * x, s"h($x)=${x*x}")

  val r: Debuggable = for {
    fr <- f(100) // flatMap
    gr <- g(fr)  // flatMap
    hr <- h(gr)  // map
  } yield hr

  println(s"r.value = ${r.value}")
  println(s"r.message = ${r.message}")

}
