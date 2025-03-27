package pfps

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.{Contains, NonEmpty}
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import org.scalatest.funsuite.AnyFunSuite
import pfps.ref.NonNegativeR.NonNegativeDouble

/**
 * https://github.com/fthomas/refined
 */
object A028 extends App {
  type Username = NonEmptyString Refined Contains['g']
  def lookup(username: Username) = ???

  // we can refine only once!
  type BrandX = String Refined NonEmpty
  @newtype case class Brand(value: BrandX)
  @newtype case class Category(value: NonEmptyString)

  val brand: Brand = Brand("foo")
  println(brand)
}

class A028Spec extends AnyFunSuite {

  test("code should not compile 1") {
    assertDoesNotCompile("val x: String = 123")
  }

  test("code should not compile 2") {
    assertTypeError("val y: List[Int] = List(\"oops\")")
  }

  test("1") {
    val x = math.sqrt(-1)
    pprint.log(x)
  }

  test("2a") {
    import eu.timepit.refined._
    import eu.timepit.refined.api.Refined
    import eu.timepit.refined.auto._
    import eu.timepit.refined.numeric._
    import eu.timepit.refined._
    import eu.timepit.refined.auto._
    import eu.timepit.refined.numeric._
    import eu.timepit.refined.api.{RefType, Refined}
    import eu.timepit.refined.boolean._
    import eu.timepit.refined.char._
    import eu.timepit.refined.collection._
    import eu.timepit.refined.generic._
    import eu.timepit.refined.string._
    import shapeless.{ ::, HNil }

    // will not compile
//    val x1: Refined[Int, Interval.ClosedOpen[7, 77]] = 77
    val x1: Refined[Int, Interval.ClosedOpen[7, 77]] = 76
  }

  test("2b") {
    import eu.timepit.refined._
    import eu.timepit.refined.api.Refined
    import eu.timepit.refined.api.Validate
    import eu.timepit.refined.auto._
    import eu.timepit.refined.numeric._
    import eu.timepit.refined._
    import eu.timepit.refined.auto._
    import eu.timepit.refined.numeric._
    import eu.timepit.refined.api.{RefType, Refined}
    import eu.timepit.refined.boolean._
    import eu.timepit.refined.char._
    import eu.timepit.refined.collection._
    import eu.timepit.refined.generic._
    import eu.timepit.refined.string._
    import shapeless.{ ::, HNil }

    type NonNeg = Refined[Double, NonNegative]

    val valid: Either[String, NonNeg] = refineV[NonNegative](3.14)
    val invalid: Either[String, NonNeg] = refineV[NonNegative](-2.0)
    pprint.log(valid)
    pprint.log(invalid)


    def f1(x: NonNeg): Double = math.sqrt(1)

    val y = f1(1.0)
    pprint.log(y)

//    val y2 = f1(-1.0)
  }


}