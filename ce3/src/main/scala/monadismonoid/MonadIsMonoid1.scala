package monadismonoid

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** useful utf8 characters: https://cloford.com/resources/charcodes/utf-8_mathematical.htm */
object MonadIsMonoid1 {

  /** 4. monoid in a monoidal category, monoid in category A (actually K1) */
  trait MonoidInCategory[A, ~>[_, _], U, P] {
    def unit: U ~> A
    def combine: P ~> A
  }

  /** 3. general monoid, functions, `unit` and `combine` are inherited */
  trait GeneralMonoid[A, U, P] extends MonoidInCategory[A, Function1, U, P]

  /** 2. functional monoid, `unit` and `combine` are inherited */
  trait FunctionalMonoid[A] extends GeneralMonoid[A, Unit, (A, A)]

  /** 1. monoid,
    * rules:
    *
    * - (a ⊕ b) ⊕ c = a ⊕ (b ⊕ c)
    *
    * - a ⊕ empty = empty ⊕ a
    */
  trait Monoid[A] extends FunctionalMonoid[A] {
    def empty: A
    def combine(a1: A, a2: A): A
    // 2. define inherited ones in terms on current API
    def unit: Unit => A = _ => empty
    def combine: ((A, A)) => A = (aa: (A, A)) => combine(aa._1, aa._2)
  }

}

object InstancesMonoid {

  import MonadIsMonoid1._

  implicit val monoidIntSum: Monoid[Int] = new Monoid[Int] {
    override def empty: Int = 0
    override def combine(a1: Int, a2: Int): Int = a1 + a2
  }

  implicit val monoidStringConcat: Monoid[String] = new Monoid[String] {
    override def empty: String = ""
    override def combine(a1: String, a2: String): String = a1.concat(a2)
  }

  case class ShoppingCart(items: Int, total: Double)
  implicit val monoidCart: Monoid[ShoppingCart] = new Monoid[ShoppingCart] {
    override def empty: ShoppingCart = ShoppingCart(0, 0.0)
    override def combine(a1: ShoppingCart, a2: ShoppingCart): ShoppingCart = ShoppingCart(a1.items + a2.items, a1.total + a2.total)
  }

}

class MonadIsMonoid1Spec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {
  import MonadIsMonoid1._
  import InstancesMonoid._

  def combine[A](as: List[A])(implicit M: Monoid[A]): A = as.foldLeft(M.empty)(M.combine)

  test("1.1. monoid - int - sum") {
    combine(List(1, 2, 3)) shouldEqual 6
  }

  test("1.2. monoid - string - concat") {
    combine(List("aa", "bb", "cc")) shouldEqual "aabbcc"
  }

  test("1.3. monoid - shopping cart") {
    combine(
      List(
        ShoppingCart(3, 15.99),
        ShoppingCart(5, 19.99)
      )
    ) shouldEqual ShoppingCart(8, 35.98)
  }

}
