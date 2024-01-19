package monadismonoid

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object MonadIsMonoid2 {

  /** 5. Scala's `A => B` is a syntax for `Function1[A, B]` */
  trait ==>[-A, +B] extends Function1[A, B]

  /** 6. functor, actually endofunctor, because A and B belong to the same category of Scala types */
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A ==> B): F[B]
  }

  /** 7. trick to treat type `A` as `A[_]` */
  type Id[A] = A

}

object InstancesFunctor {

  import MonadIsMonoid2._

  implicit val optFunctor: Functor[Option] = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A ==> B): Option[B] = fa.map(f)
  }

  implicit val listFunctor: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A ==> B): List[B] = fa.map(f)
  }

  implicit val idFunctor: Functor[Id] = new Functor[Id] {
    override def map[A, B](fa: Id[A])(f: A ==> B): Id[B] = f(fa)
  }

}

class MonadIsMonoid2Spec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import MonadIsMonoid2._

  /** we can create functor for:
    * - Option
    * - List
    * - Either
    * - Parser
    * - Future
    * - IO
    * ...
    */

  test("5. function creation") {

    val f1: Int ==> String = new ==>[Int, String] {
      override def apply(a: Int): String = a.toString
    }

    f1.apply(123) shouldBe "123"
    f1(1234) shouldBe "1234"

  }

  import InstancesFunctor._

  test("6.1. functor creation / usage - option") {
    optFunctor.map(Some(32))(_ + 10) shouldBe Some(42)
  }

  test("6.2. functor creation / usage - list") {
    listFunctor.map(List(1, 2, 3))(_ + 10) shouldBe List(11, 12, 13)
  }

  /** WHY do we need that ? WE can unify mapping behavior not to things by having their */
  implicit class FunctorOps[F[_], A](fa: F[A]) {
    def fmap[B](f: A ==> B)(implicit ff: Functor[F]): F[B] = ff.map(fa)(f)
  }

  test("6.3. functor syntax") {
    List(1, 2, 3).fmap(_ + 1) shouldBe List(2, 3, 4)
    Option("scala").fmap(_.toUpperCase) shouldBe Some("SCALA")
  }

  test("7. Functor[Id]") {
    val x: Id[Int] = 33
    x.fmap(_ + 1) shouldBe 34
  }
}
