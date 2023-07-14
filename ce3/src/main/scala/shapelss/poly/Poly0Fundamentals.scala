package shapelss.poly

import shapelss.poly.Business.intToString
import shapelss.poly.Business.stringToInt

object Poly0Fundamentals extends App {

  trait Poly {
    def apply[A, B](x: A)(implicit cse: Function1[A, B]): B = cse(x)
  }

  object f extends Poly {

    implicit val intCase = new Function1[Int, String] {
      def apply(x: Int) = intToString(x)
    }

    implicit val stringCase = new Function1[String, Int] {
      def apply(x: String) = stringToInt(x)
    }

  }

  /** but here are the problem:
    *   - we cant mix different functions
    *   - we must care about proper imports
    */
  import f._

  val r1: String = f(3)
  val r2: Int = f("Hello")

}
