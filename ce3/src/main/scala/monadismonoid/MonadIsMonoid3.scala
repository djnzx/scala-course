package monadismonoid

import monadismonoid.MonadIsMonoid3.NaturalTransformation
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object MonadIsMonoid3 {

  /** 8. Natural Transformation between functors */
  trait NaturalTransformation[-F[_], +G[_]] { // F[_] ~> G[_]
    def apply[A](fa: F[A]): G[A]
  }

}

object InstancesNaturalTransformationSimple {

  object ListToOption extends NaturalTransformation[List, Option] {
    override def apply[A](fa: List[A]): Option[A] = fa.headOption
  }

  object OptionToList extends NaturalTransformation[Option, List] {
    override def apply[A](fa: Option[A]): List[A] = fa.toList
  }

  object OptionToEitherR extends NaturalTransformation[Option, Either[Nothing, *]] {
    override def apply[A](fa: Option[A]): Either[Nothing, A] = fa.toRight(???)
  }

  object OptionToEitherL extends NaturalTransformation[Option, Either[*, Nothing]] {
    override def apply[A](fa: Option[A]): Either[A, Nothing] = fa.toLeft(???)
  }

  object EitherRToOption extends NaturalTransformation[Either[Any, *], Option] {
    override def apply[A](fa: Either[Any, A]): Option[A] = fa match {
      case Right(a) => Some(a)
      case Left(_)  => None
    }
  }

  object EitherLToOption extends NaturalTransformation[Either[*, Any], Option] {
    override def apply[A](fa: Either[A, Any]): Option[A] = fa match {
      case Left(a)  => Some(a)
      case Right(_) => None
    }
  }

}

class MonadIsMonoid3Spec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  test("8. simple natural transformation: F[_] <-> G[_]: list - option - either") {
    import InstancesNaturalTransformationSimple._

    ListToOption(List(1, 2, 3)) shouldBe Some(1)
    ListToOption(List(1)) shouldBe Some(1)
    ListToOption(List.empty) shouldBe None

    OptionToList(None) shouldBe List.empty
    OptionToList(Some(42)) shouldBe List(42)

    OptionToEitherR(Some(33)) shouldBe Right(33)
    OptionToEitherL(Some(33)) shouldBe Left(33)

    EitherLToOption(Left(11)) shouldBe Some(11)
    EitherLToOption(Right("bla")) shouldBe None

    EitherRToOption(Right("bla")) shouldBe Some("bla")
    EitherRToOption(Left(11)) shouldBe None
  }

}
