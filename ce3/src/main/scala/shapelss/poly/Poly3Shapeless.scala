package shapelss.poly

import shapeless.ops.coproduct.Inject
import shapeless.{:+:, CNil, Coproduct, Poly1}

/** https://akmetiuk.com */
object Poly3Shapeless extends App {

  def printMe(s: String) = pprint.pprintln(s)

  type X = Int :+: Double :+: Boolean :+: CNil
  object X {
    def apply[A](a: A)(implicit inj: Inject[X, A]): X = inj(a)
  }

  val x1: X = X(1)
  val x2: X = X(3.14)
  val x3: X = X(true)
  val x4: X = Coproduct[X](1) // now, we don't need `X.apply`

  val fi = (x: Int) => s"I'm an Int: $x"
  val fd = (x: Double) => s"I'm a Double: $x"
  val fb = (x: Boolean) => s"I'm a Boolean: $x"

  object f1 extends Poly1 {
    implicit val _i = at[Int](fi)
    implicit val _d = at[Double](fd)
    implicit val _b = at[Boolean](fb)
  }

  printMe(x1.fold(f1))
  printMe(x2.fold(f1))
  printMe(x3.fold(f1))

  val f2 = Poly1
    .at[Int](fi)
    .at[Double](fd)
    .at[Boolean](fb)
    .build

}
