package shapelss.poly

import shapelss.poly.Business.intToString
import shapelss.poly.Business.stringToInt

object Poly0Fundamentals extends App {

  trait Poly {
    def apply[A, B](x: A)(implicit f: A => B): B = f(x)
  }

  object f extends Poly {
    implicit val intToStringCase: Int => String = (x: Int) => intToString(x)
    implicit val intToDoubleCase: Int => Double = (x: Int) => x.toDouble
    implicit val stringToIntCase: String => Int = (x: String) => stringToInt(x)
  }

  /** but here are the problem:
    *   - we cant mix different functions
    *   - we must care about proper imports
    */
  import f._

  val r1: String = f(3)
  val r2: Double = f(5)
  val r3: Int = f("Hello")

}
