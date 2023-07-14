package shapelss.poly

object Poly2RemovingOutputType extends App {

  /** we can get rid of type B if we want */
  trait Case[F, A] {
    type B
    def apply(x: A): B
  }

  trait Poly {
    def apply[A, B](x: A)(implicit cse: Case[this.type, A]): cse.B = cse(x)
  }

  object f extends Poly {

    implicit val intCase = new Case[f.type, Int] {
      type B = String
      def apply(x: Int) = "It works! " * x
    }

    implicit val stringCase = new Case[f.type, String] {
      type B = Int
      def apply(x: String) = x.length
    }

  }

  val r1: String = f(3)
  val r2: Int = f("Hello")

}
