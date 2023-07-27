package shapelss.poly

object Poly1BringingContext extends App {

  def intToString(x: Int) = "It works! " * x
  def stringToInt(x: String) = x.length

  /** we bring type F:
    *   - to wire the context
    *   - eliminate the import
    */
  trait Case[F, A, B] {
    def apply(a: A): B
  }

  trait Poly {
    def apply[A, B](x: A)(implicit cse: Case[this.type, A, B]): B = cse(x)
  }

  object f extends Poly {
    implicit val f1: Case[f.type, Int, String] = intToString
    implicit val f2: Case[f.type, String, Int] = stringToInt
  }

  val r1: String = f(3)
  val r2: Int = f("Hello")

  println(r1)
  println(r2)
}
