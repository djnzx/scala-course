package monadismonoid

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object MonadIsMonoid4 {
  import MonadIsMonoid2._
  import MonadIsMonoid3._

  /** 9. move to one kind higher. replace all the types `A` with `A[_]`, monoid in category A[_] */
  trait MonoidInCategoryK2[A[_], ~>[_[_], _[_]], U[_], P[_]] {
    def unit: U ~> A
    def combine: P ~> A
  }

  def normalFunctionComposition[A, B, C](f: A => B, g: B => C): A => C = (a: A) => g(f(a))
  type HigherKindTComposition[F[_], G[_], A] = G[F[A]]
  type SameKindComposition1[F[_], A] = F[F[A]]
  type SameKindComposition2[F[_], A] = HigherKindTComposition[F, F, A]

  /** 10. monoid in category */
//trait MonoidInCategoryFunctors3[F[_]] extends MonoidInCategoryK2[F, NaturalTransformation, Id, [A] =>> F[F[A]]]] // Scala3
  trait MonoidInCategoryFunctors1[F[_]] extends MonoidInCategoryK2[F, NaturalTransformation, Id, ({ type x[A] = F[F[A]] })#x]
  trait MonoidInCategoryFunctors2[F[_]] extends MonoidInCategoryK2[F, NaturalTransformation, Id, Lambda[A => F[F[A]]]] {
    // just for clarification what we have here
    type FF[A] = F[F[A]]
    def unit: NaturalTransformation[Id, F]
    def combine: NaturalTransformation[FF, F]

    def pure[A](a: A): F[A] = unit(a)
    def flatMap[A, B](fa: F[A])(f: A ==> F[B])(implicit ff: Functor[F]): F[B] = {
      val ffb = ff.map(fa)(f)
      combine(ffb)
    }
  }

  /** 11. this is the proof vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv */
  trait Monad[F[_]] extends Functor[F] with MonoidInCategoryK2[F, NaturalTransformation, Id, Lambda[A => F[F[A]]]] {
    type FF[A] = F[F[A]]
    // public
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A ==> F[B]): F[B]
    // from functor, but we can implement it here
    override def map[A, B](fa: F[A])(f: A ==> B): F[B] = flatMap(fa)(a => pure(f(a)))
    // can be derived
    def flatten[A](ffa: F[F[A]]): F[A] = flatMap(ffa)(identity)
    // can be implemented easily, but it requires `pure` implementation
    def unit: NaturalTransformation[Id, F] = new NaturalTransformation[Id, F] {
      override def apply[A](fa: Id[A]): F[A] = pure(fa)
    }
    // can be implemented easily, but it requires `flatten` (flatMap) implementation
    def combine: NaturalTransformation[FF, F] = new NaturalTransformation[FF, F] {
      override def apply[A](fa: F[F[A]]): F[A] = flatten(fa)
    }
  }

}

object InstanceMonoidInCategoryFunctors {
  import MonadIsMonoid2._
  import MonadIsMonoid3._
  import MonadIsMonoid4._

  type ListList[A] = List[List[A]]
  object AnotherListMonoid extends MonoidInCategoryFunctors2[List] { // TODO:  with Monad[List]

    override def unit: NaturalTransformation[Id, List] = new NaturalTransformation[Id, List] {
      override def apply[A](fa: Id[A]): List[A] = List(fa)
    }

    override def combine: NaturalTransformation[ListList, List] = new NaturalTransformation[ListList, List] {
      override def apply[A](fa: List[List[A]]): List[A] = fa.flatten
    }
  }
}

class MonadIsMonoid4Spec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import InstanceMonoidInCategoryFunctors._
  import MonadIsMonoid2._
  import MonadIsMonoid3._

  test("10. natural transformation: A => F[A] - identity: A => List[A]") {
    val natTrIdentity: NaturalTransformation[Id, List] = AnotherListMonoid.unit
    natTrIdentity(33) shouldBe List[Int](33)
    AnotherListMonoid.pure(42) shouldBe List(42)
  }

  test("10. natural transformation: F[F[A]] => F[A] - flatten: List[List[A]] => List[A]") {
    val natTrFlatten: NaturalTransformation[ListList, List] = AnotherListMonoid.combine
    natTrFlatten(
      List(
        List(1, 2, 3),
        List(4, 5),
        List(6)
      )
    ) shouldBe List[Int](1, 2, 3, 4, 5, 6)
  }

  test("10. natural transformation: F[F[A]] => F[A] - flatMap !") {
    import InstancesFunctor._
    AnotherListMonoid.flatMap(
      List(1, 2, 3)
    ) { x =>
      List(-x, x)
    } shouldBe List(-1, 1, -2, 2, -3, 3)
  }

}
