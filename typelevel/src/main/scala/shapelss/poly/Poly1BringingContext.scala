package shapelss.poly

import shapelss.poly.Business.intToString
import shapelss.poly.Business.stringToInt

object Poly1BringingContext extends App {

  /** we bring type F:
    *   - to wire the context
    *   - eliminate the import
    */
  trait Case[F, A, B] {
    def apply(x: A): B
  }

  trait Poly {
    def apply[A, B](x: A)(implicit cse: Case[this.type, A, B]): B = cse(x)
  }

  object f extends Poly {
    implicit val intCase: Case[f.type, Int, String] = (x: Int) => intToString(x)
    implicit val stringCase: Case[f.type, String, Int] = (x: String) => stringToInt(x)
  }

  val r1: String = f.apply(3)
  val r2: Int = f("Hello")
}
