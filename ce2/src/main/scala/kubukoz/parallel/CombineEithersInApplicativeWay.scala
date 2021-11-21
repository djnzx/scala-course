package kubukoz.parallel

import cats.data.Validated
import cats.implicits.catsSyntaxEitherId
import cats.implicits.catsSyntaxTuple2Semigroupal
import cats.Applicative
import cats.~>

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object TypeAliases {

  type LS = List[String]
  type EitherLS[A] = Either[LS, A]
  type ValidatedLS[A] = Validated[LS, A]

}

object CombineEithersInApplicativeWay {
  import TypeAliases._

  /** first implementation */
  def combine1[A, B, C](
      e1: EitherLS[A],
      e2: EitherLS[B],
    )(f: (A, B) => C,
    )(
      implicit
      to: EitherLS ~> ValidatedLS,
      from: ValidatedLS ~> EitherLS,
    ): EitherLS[C] =
    from(
      (to(e1), to(e2)).mapN(f),
    )

  /** second implementation, abstracting on the source type */
  def combine2[A, B, C, F[_]](
      e1: F[A],
      e2: F[B],
    )(f: (A, B) => C,
    )(
      implicit
      to: F ~> ValidatedLS,
      from: ValidatedLS ~> F,
    ): F[C] =
    from(
      (to(e1), to(e2)).mapN(f),
    )

  /** third implementation, abstracting on the intermediate type now,
    *
    * we can compose everything, almost regardless their types
    */
  def combine3[A, B, C, F[_], M[_]: Applicative](e1: F[A], e2: F[B])(f: (A, B) => C)(to: F ~> M, from: M ~> F): F[C] =
    from(
      (to(e1), to(e2)).mapN(f),
    )

}

object Implicits {
  import TypeAliases._

  implicit val ev: EitherLS ~> ValidatedLS = new (EitherLS ~> ValidatedLS) {
    override def apply[A](fa: EitherLS[A]): ValidatedLS[A] = Validated.fromEither(fa)
  }

  implicit val ve: ValidatedLS ~> EitherLS = new (ValidatedLS ~> EitherLS) {
    override def apply[A](fa: ValidatedLS[A]): EitherLS[A] = fa.toEither
  }

}

class CombineEithersInApplicativeWaySpec extends AnyFunSpec with Matchers {

  import TypeAliases._
  import CombineEithersInApplicativeWay._
  import Implicits._

  val v1 = 10.asRight[LS]
  val v2 = true.asRight[LS]
  val v3 = "Jim".asRight[LS]

  val v4 = List("error1").asLeft[Int]
  val v5 = List("error2").asLeft[String]

  val expectedCombination = List("error1", "error2")

  it("0") {
    combine1(v1, v2)((_, _)) shouldEqual Right(10 -> true)
  }

  it("1") {
    combine1(v4, v5)((_, _)) shouldEqual Left(expectedCombination)
  }

  it("2") {
    combine2(v4, v5)((_, _)) shouldEqual Left(expectedCombination)
  }

  it("3") {
    combine3(v4, v5)((_, _))(ev, ve) shouldEqual Left(expectedCombination)
  }

}
