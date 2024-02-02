package scalacheck

import cats.Functor
import cats.implicits._
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

trait WrappersIdeas {

  trait Value[A] {
    val value: A
    val v: A = value
  }

  case class IsTest(value: Boolean) extends Value[Boolean]

  implicit class UnwrapFunctorOps[F[_]: Functor, A](fa: F[A]) {
    def v[B](implicit ev: A <:< Value[B]): F[B] = fa.map(_.value)
  }

}

object WrappersIdeas extends WrappersIdeas

class WrappersIdeasSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  import WrappersIdeas._

  test("syntax ideas") {

    IsTest(
      true
    ).v shouldBe true

    Option(
      IsTest(true)
    ).v shouldBe Option(true)

    List(
      IsTest(true),
      IsTest(false)
    ).v shouldBe List(true, false)

  }

}
