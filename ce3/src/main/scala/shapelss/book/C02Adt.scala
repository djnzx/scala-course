package shapelss.book

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import pprint.{pprintln => println}
import shapeless._

/** https://books.underscore.io/shapeless-guide/shapeless-guide.html#sec:representations */
object C02Adt extends App {

  object ClassicAdtApproach {

    /** representation */
    sealed trait Shape

    final case class Rectangle(width: Double, height: Double) extends Shape

    final case class Circle(radius: Double) extends Shape

    /** instances */
    val rect: Shape = Rectangle(3.0, 4.0)
    val circ: Shape = Circle(1.0)

    /** naive implementation */
    def area(shape: Shape): Double =
      shape match {
        case Rectangle(w, h) => w * h
        case Circle(r)       => math.Pi * r * r
      }

  }

  object AlternativeApproach {

    /** alternative ways */
    type Rectangle = (Double, Double)
    type Circle = Double
    type Shape = Either[Rectangle, Circle]

    val rect: Shape = Left((3.0, 4.0))
    val circ: Shape = Right(1.0)

    def area2(shape: Shape): Double =
      shape match {
        case Left((w, h)) => w * h
        case Right(r)     => math.Pi * r * r
      }

  }

  object AccessingElements {
    val productOf3a: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil
    val productOf3b: String :: Int :: Boolean :: HNil = "Sunday" :: (1 :: (false :: HNil))

    /** will not compile, due to absence of 3-th index */
//    val x = productOf3a(3)

    val productOf4: Long :: String :: Int :: Boolean :: HNil = 42L :: productOf3a
    val fourth: Boolean = productOf4(3)
  }

  object RealUsecase {

    /** case class */
    case class IceCream(name: String, numCherries: Int, inCone: Boolean)
    case class Employee(name: String, number: Int, manager: Boolean)

    /** convertor */
    val iceCreamGen = Generic[IceCream]
    val employeeGen = Generic[Employee]

    /** instance */
    val iceCream: IceCream = IceCream("Sundae", 1, false)

    /** a typed data extracted from a case class */
    val repr: String :: Int :: Boolean :: HNil = iceCreamGen.to(iceCream)

    /** new instance created from a RAW data */
    val iceCream2: IceCream = iceCreamGen.from(repr)

    /** decomposing HList */
    val head: String = repr.head
    val tail: Int :: Boolean :: HNil = repr.tail

    /** converting back to the same structure */
    val e: Employee = employeeGen.from(repr)

  }

  object Coproducts {

    case class Red()
    case class Amber()
    case class Green()
    type Light = Red :+: Amber :+: Green :+: CNil // :+: === or

    val red: Light = Inl(Red())
    val green: Light = Inr(Inr(Inl(Green())))

  }

  object GenericsEncodings {
    import ClassicAdtApproach._

    /** 2.3.1 Switching encodings using Generic */
    val gen = Generic[Shape] // it will create type fo us combining all subtypes of given sealed trait
    val r3 = gen.to(Rectangle(3.0, 4.0))
    val r4 = gen.to(Circle(1.0))
  }

}

class C02AdtSpec extends AnyFunSpec with Matchers {

  it("1") {
    import C02Adt.GenericsEncodings._

    println(gen)
    println(r3) // Inr(tail = Inl(head = Rectangle(width = 3.0, height = 4.0)))
    println(r4) // Inl(head = Circle(radius = 1.0))
  }

}
