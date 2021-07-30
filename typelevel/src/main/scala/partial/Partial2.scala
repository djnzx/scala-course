package partial

object Partial2 extends App {

  def validated(x: Int) = x < 10

  def f1: PartialFunction[Int, String] = {
    case 1 => "one"
    case 10 => "ten"
    case 2 => throw new IllegalArgumentException("TWO")
  }

  def composition: PartialFunction[Int, String] = {
    case x if validated(x) => f1(x)
  }

  lazy val a = composition(1)  // one, f1 works
  lazy val b = composition(2)  // IllegalArgumentException(TWO), theown by f1
  lazy val c = composition(3)  // MatchError, f1 is broken, because it's partial
  lazy val d = composition(10) // MatchRrror, composition is broken

  println(d)

}
