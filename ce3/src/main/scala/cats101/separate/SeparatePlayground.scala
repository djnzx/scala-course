package cats101.separate

import cats.Bifoldable
import cats.Eval
import cats.data.Ior
import cats.implicits._
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** the concept is to separate things which are generic in two holes:
  * Tuple2[A, B]
  * Either[E, A]
  * Ior[A, B]
  * ...
  */
class SeparatePlayground extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("Either") {
    val xs = List(
      "error1".asLeft,
      5.asRight,
      "error2".asLeft,
      33.asRight
    )

    // requires: FlatMap[List], Alternative[List], Bifoldable[Either]
    val (ls, rs) = xs.separate
    // requires: Foldable[List], Alternative[List], Bifoldable[Either]
    val (ls2, rs2) = xs.separateFoldable

    ls shouldBe List("error1", "error2")
    rs shouldBe List(5, 33)
  }

  test("Tuple2") {
    val (ls, rs) = List(
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    ).separate

    ls shouldBe List("a", "b", "c")
    rs shouldBe List(1, 2, 3)
  }

  test("Ior") {
    val a: Ior[String, Int] = Ior.Left("A")
    val b: Ior[String, Int] = Ior.Right(3)
    val c: Ior[String, Int] = Ior.Both("C", 13)

    val (ls, rs) = List(a, b, c).separate

    ls shouldBe List("A", "C")
    rs shouldBe List(3, 13)
  }

  test("biFoldable playground") {
    val fab: (List[Int], Int) = (List(1), 2)

    val x: Option[Int] =
      Bifoldable[Tuple2].bifoldLeft(
        fab,
        Option(5)
      )(
        (c, a) => c.map(i => i + a.head),
        (c, b) => c.map(i => i + b)
      )

    pprint.log(x)
  }

  test("custom two hole things") {
    sealed trait Both[+A, +B]
    final case class L[A](a: A) extends Both[A, Nothing]
    final case class R[B](b: B) extends Both[Nothing, B]
    final case object X         extends Both[Nothing, Nothing]
    object Both {
      def l[A](a: A): Both[A, Nothing] = L(a)
      def r[B](b: B): Both[Nothing, B] = R(b)
      def x: Both[Nothing, Nothing] = X
    }
    import Both._

    val xs: List[Both[Int, Double]] = List(
      l(1),
      r(2.5),
      l(10),
      r(3.14),
      x
    )

    /** basically we need to pattern match and apply functions */
    implicit val bfBoth: Bifoldable[Both] = new Bifoldable[Both] {

      override def bifoldLeft[A, B, C](
          fab: Both[A, B], // x
          c: C             // default empty value for Monoid[List]
        )(f: (C, A) => C,
          g: (C, B) => C
        ): C = {
        pprint.log(fab)
        pprint.log(c)

        val c2 = fab match {
          case L(a) => f(c, a)
          case R(b) => g(c, b)
          case X    => c
        }
        pprint.log(c2)
        println("--")
        c2
      }

      override def bifoldRight[A, B, C](
          fab: Both[A, B],
          c: Eval[C]
        )(f: (A, Eval[C]) => Eval[C],
          g: (B, Eval[C]) => Eval[C]
        ): Eval[C] =
        fab match {
          case L(a) => f(a, c)
          case R(b) => g(b, c)
          case X    => c
        }

    }

    // it relies on FlatMap[List], so we have 2 passes
    val (ls, rs) = xs.separate
    println("----------------")

    // it relies on Foldable[List], so we have 1 pass
    val (ls2, rs2) = xs.separateFoldable
    println("----------------")

    ls shouldBe List(1, 10)
    rs shouldBe List(2.5, 3.14)
  }

}
