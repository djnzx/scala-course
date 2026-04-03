package yoneda

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlaygroundList extends AnyFunSuite with Matchers {

  val xs = List(1, 2, 3)

  trait LazyFunctor[A] { self =>
    // must be defined for the structure _outside_, since we don't have access to the structure
    def transform[B](f: A => B): List[B]

    // collects and keeps transformation
    def map[B](f: A => B): LazyFunctor[B] = new LazyFunctor[B] {
      def transform[C](g: B => C): List[C] = self.transform(f andThen g)
    }

    // runs transformation
    def value: List[A] = transform(identity)

  }

  implicit class ToLazySyntax[A](as: List[A]) {
    def toLazy: LazyFunctor[A] = new LazyFunctor[A] {
      def transform[B](f: A => B): List[B] = as.map(f)
    }
  }

  test("complexity: 2 * N") {
    val ys = xs
      .map(_ + 2) // 3, 4, 5
      .map(_ * 2) // 6, 8, 10

    ys shouldBe List(6, 8, 10)
  }

  test("complexity: N") {
    val ys = xs
      .toLazy
      .map(_ + 2) // 3, 4, 5
      .map(_ * 2) // 6, 8, 10
      .value

    ys shouldBe List(6, 8, 10)
  }

}
