package cats101.profunctor

import cats._
import cats.data._
import cats.implicits._
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ProfunctorPlayground extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  // plain functions
  def mainF(a: Int): Float = a.toFloat
  def pre(a: String): Int = a.length
  def post(a: Float): Double = (a + 1).toDouble

  test("scala plain combination") {

    // main function:          Int => Float
    // pre function: String => Int
    // post function:                 Float => Double

    val f: String => Double = (pre _) andThen mainF andThen post

    f("hello") shouldBe 6.0
  }

  test("functor based combination (post only)") {
    val f: Int => Double = (mainF _).map(post)

    f(5) shouldBe 5.0d
  }

  test("profunctor based combination") {
    val f: String => Double = (mainF _).dimap(pre)(post)

    f("Jim") shouldBe 4.0d
  }

  test("profunctor based combination (pre only)") {
    val f: String => Float = (mainF _).lmap(pre)

    f("Jim") shouldBe 3.0f
  }

  test("profunctor based combination (post only)") {
    val f: Int => Double = (mainF _).rmap(post)

    f(3) shouldBe 4.0d
  }

  /** - prepend is impossible by standard Scala syntax
    * - cats contravariant requires something with one hole F[_]
    */
  test("cats contravariant prepend") {
    type ToString[A] = A => String
    val f: ToString[Int] = (x: Int) => s"original: $x"

    val fPrepended: ToString[String] = f.contramap(pre)

    fPrepended("hello") shouldBe "original: 5"
  }

  test("playground") {

    def mkFunctionContravariant[C]: Contravariant[* => C] =
      new Contravariant[* => C] {
        override def contramap[A, B](fac: A => C)(fba: B => A): B => C =
          (b: B) => {
            val a: A = fba(b)
            val c: C = fac(a)
            c
          }
      }

    val f: String => Float = mkFunctionContravariant.contramap(mainF)(pre)

  }

}
