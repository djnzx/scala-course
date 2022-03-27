package shapelesscoproduct

import shapeless.:+:
import shapeless.CNil
import shapeless.Poly1
import shapeless.ops.coproduct.Inject

import scala.reflect.runtime.universe.reify

/** https://akmetiuk.com */
object Poly3Shapeless extends App {

  def printMe(s: String) = pprint.pprintln(s)

  type X = Int :+: Double :+: Boolean :+: CNil
  object X {
    def apply[A](a: A)(implicit inj: Inject[X, A]): X = inj(a)
  }
  // my data
  val x1: X = X(1)
  val x2: X = X(3.14)
  val x3: X = X(true)
  // my function to fold
  val fi = (x: Int) => s"I'm an Int: $x"
  val fd = (x: Double) => s"I'm a Double: $x"
  val fb = (x: Boolean) => s"I'm a Boolean: $x"

  {

    /** if we create it via the object,
      *   - context propagates automatically
      *   - we don't need the import
      *   - we can have as many objects as we need
      */
    object f1 extends Poly1 {
      implicit val _i = at[Int](fi)
      implicit val _d = at[Double](fd)
      implicit val _b = at[Boolean](fb)
    }

    /** applying function to RAW value */
    println(f1(123))
    println(f1(3.14))
    println(f1(true))

    /** folding coproduct */
    printMe(x1.fold(f1))
    printMe(x2.fold(f1))
    printMe(x3.fold(f1))
  }

  {

    /** if we create it via builder
      *   - context doesn't propagate automatically
      *   - we need to have an import
      *   - we can't have more than one function in the scope
      */
    val f2 = Poly1
      .at[Int](fi)
      .at[Double](fd)
      .at[Boolean](fb)
      .build
    import f2._

    /** applying function to RAW value */
    println(f2(123))
    println(f2(3.14))
    println(f2(true))

    /** folding coproduct */
    printMe(x1.fold(f2))
    printMe(x2.fold(f2))
    printMe(x3.fold(f2))

    val mapped = x1.map(f2)
    pprint.pprintln(reify(mapped))
  }

}
