package yoneda

import cats.Functor
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlaygroundFunctor extends AnyFunSuite with Matchers {

  trait LazyFunctor[F[_], A] { outer =>
    // must be defined for the structure _outside_, since we don't have access to the structure
    def transform[B](f: A => B): F[B]

    // collects and keeps transformation
    def map[B](f: A => B): LazyFunctor[F, B] = new LazyFunctor[F, B] {
      def transform[C](g: B => C): F[C] = outer.transform(f andThen g)
    }

    // runs transformation
    def value: F[A] = transform(identity)

  }

  object LazyFunctor {
    def apply[F[_], A](fa: F[A])(implicit FF: Functor[F]): LazyFunctor[F, A] =
      new LazyFunctor[F, A] {
        def transform[B](f: A => B): F[B] = FF.map(fa)(f)
      }
  }

  implicit class ToLazySyntax[F[_]: Functor, A](fa: F[A]) {
    def toLazy = LazyFunctor(fa)
  }

  test("complexity: 2 * N") {
    val ys = List(1, 2, 3)
      .map(_ + 2) // 3, 4, 5
      .map(_ * 2) // 6, 8, 10

    ys shouldBe List(6, 8, 10)
  }

  test("complexity: N (one traversal)") {
    List(1, 2, 3)
      .toLazy
      .map(_ + 2)
      .map(_ * 2)
      .value shouldBe List(6, 8, 10)
  }

}
