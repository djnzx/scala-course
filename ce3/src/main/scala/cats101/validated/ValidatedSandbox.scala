package cats101.validated

import cats.data.Validated.{Invalid, Valid}
import cats.data._
import cats.implicits._
import cats.kernel.Semigroup
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ValidatedSandbox extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("plain |+|, Semigroup (Monoid fits as well)") {
    // implementation from Semigroup[Int]
    1 |+| 2 shouldBe 3
    // implementation from Monoid[List]
    List(1, 2) |+| List(3, 4) shouldBe List(1, 2, 3, 4)

    // custom type
    case class ShoppingCart(items: Int, total: Double)
    // custom implementation
    implicit val semiCart: Semigroup[ShoppingCart] = new Semigroup[ShoppingCart] {
      override def combine(a1: ShoppingCart, a2: ShoppingCart): ShoppingCart =
        ShoppingCart(a1.items + a2.items, a1.total + a2.total)
    }
    // use in the same way
    ShoppingCart(3, 4.99) |+| ShoppingCart(4, 7.99) shouldBe ShoppingCart(7, 12.98)

    List(
      ShoppingCart(3, 4.99),
      ShoppingCart(4, 7.99)
    ).combineAllOption shouldBe ShoppingCart(7, 12.98).some

    /** if we have monoid - we can use .combine without Option
      * since monoid has "empty" element
      */

    List[ShoppingCart](
    ).combineAllOption shouldBe None
  }

  test("plain <+>, SemigroupK (in any category F[_])") {
    // SemigroupK
    List(1, 2) <+> List(3, 4) shouldBe List(1, 2, 3, 4)
  }

  test("validated <+> (orElse semantics)") {
    case class Box(x: Int)

    type V = ValidatedNel[String, Box]

    val x1: V = Box(3).valid
    val x2: V = Box(5).valid
    val x3: V = NonEmptyList.of("E1", "E2").invalid
    val x4: V = NonEmptyList.of("E3", "E4").invalid

    /** - It has orElse semantics on the right side.
      *   If any is Valid => the result is Valid
      * - It combines the left parts (requires combineK),
      *   like List(1,2) <+> List(3,4) == List(1,2,3,4)
      *   only when both are Invalid
      */
    val a = x1 <+> x2
    val b = x2 <+> x3
    val c = x3 <+> x4

    pprint.log(a)
    pprint.log(b)
    pprint.log(c)

    a shouldBe Box(3).valid
    b shouldBe Box(5).valid
    c shouldBe NonEmptyList.of("E1", "E2", "E3", "E4").invalid
  }

  test("validated sequence") {
    case class Box(x: Int)
    type V = ValidatedNel[String, Box]
    val x1: V = Box(3).valid
    val x2: V = Box(5).valid
    val x3: V = NonEmptyList.of("E1", "E2").invalid
    val x4: V = NonEmptyList.of("E3", "E4").invalid

    List(x1, x2).sequence shouldBe List(Box(3), Box(5)).valid
    List(x3, x4).sequence shouldBe NonEmptyList.of("E1", "E2", "E3", "E4").invalid
    List(x1, x2, x3, x4).sequence shouldBe NonEmptyList.of("E1", "E2", "E3", "E4").invalid
  }

  test("validated traverse") {

    def validateIt(raw: String) =
      raw.toIntOption
        .fold(raw.invalidNel[Int])(_.valid)

    /** keeps all results if they are good */
    List("1", "2", "3").traverse(validateIt) shouldBe List(1, 2, 3).valid

    /** keep all errors if at least one of them is bad */
    List("1", "2", "3", "4x", "5z").traverse(validateIt) shouldBe NonEmptyList.of("4x", "5z").invalid

  }

  test("indexed validation") {

    def mkIndexed[A](xs: Seq[A]) = LazyList.from(1) zip xs

    def validateIt(raw: String) =
      raw.toDoubleOption
        .fold(raw.invalid[Double])(_.valid)

    def validateIndexed[A, E, B](f: A => Validated[E, B], data: Seq[A]) = {
      val validated = data.map(f)
      val validatedIndexed = mkIndexed(validated)
      val validatedIndexedOnlyErrors = validatedIndexed.map {
        case (idx, Invalid(e)) => Invalid(idx -> e)
        case (_, Valid(x))     => Valid(x)
      }
      validatedIndexedOnlyErrors
        .map(_.leftMap(x => NonEmptyList.of(x)))
        .sequence
    }

    validateIndexed(validateIt, List("11", "22", "33")) shouldBe List(11.0, 22.0, 33.0).valid
    validateIndexed(validateIt, List("11", "22", "33", "boom", "blah")) shouldBe NonEmptyList.of(4 -> "boom", 5 -> "blah").invalid
  }

}
