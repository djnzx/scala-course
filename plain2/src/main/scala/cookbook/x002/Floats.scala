package cookbook.x002

object Floats extends App {
  val x1 = 0.1
  val x2 = 0.2
  val x3 = 0.3

  if (x1 + x2 == x3) println("equals!")
    else println("no equals!")

  def ~=(x: Double, y:Double, precision: Double=0.001) =
    if ((x-y).abs < precision) true else false

  implicit class PreciseComparator(d: Double) {
    def ~=(x: Double, precision: Double) = (d-x).abs < 0.01
  }

  println(~=(x1+x2, x3, 0.001))
  println(~=(x1+x2, x3))
  ~=(x1+x2, x3)
  println(x1.~=(x2,0.01f))
}
