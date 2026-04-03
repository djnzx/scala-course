package yoneda

import cats.Functor
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlaygroundFunctor2 extends AnyFunSuite with Matchers {

  class LazyFunctor[F[_], A, B](fa: F[A], fab: A => B) { x =>
    def map[C](fbc: B => C) = new LazyFunctor(fa, fab andThen fbc)
    def value(implicit FF: Functor[F]): F[B] = FF.map(fa)(fab)
  }

  implicit class ToLazySyntax[F[_], A](fa: F[A]) {
    def toLazy = new LazyFunctor(fa, identity[A])
  }

  test("complexity: N (one traversal)") {
    List(1, 2, 3)
      .toLazy
      .map(_ + 2)
      .map(_ * 2)
      .value shouldBe List(6, 8, 10)
  }

}
