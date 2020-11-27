package shapelessx

import shapeless._

object Chapter02 extends App {

  sealed trait Shape
  final case class Rectangle(width: Double, height: Double) extends Shape
  final case class Circle(radius: Double) extends Shape

  val rect: Shape = Rectangle(3.0, 4.0)
  val circ: Shape = Circle(1.0)

  def area(shape: Shape): Double =
    shape match {
      case Rectangle(w, h) => w * h
      case Circle(r)       => math.Pi * r * r
    }

  type Rectangle2 = (Double, Double)
  type Circle2    = Double
  type Shape2     = Either[Rectangle2, Circle2]

  val rect2: Shape2 = Left((3.0, 4.0))
  val circ2: Shape2 = Right(1.0)

  def area2(shape: Shape2): Double =
    shape match {
      case Left((w, h)) => w * h
      case Right(r)     => math.Pi * r * r
    }

  val product1: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil
  val product2: String :: Int :: Boolean :: HNil = "Sunday" :: (1 :: (false :: HNil))

  /** will not compile */  
//  val x = product1(3)
  
  val p4: Long :: String :: Int :: Boolean :: HNil = 42L :: product1
  /** now, will */
  val fourth = p4(3)

  /** case class */
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)
  /** convertor */
  val iceCreamGen = Generic[IceCream]
  /** instance */
  val iceCream: IceCream = IceCream("Sundae", 1, false)
  /** a typed data extracted from a case class */
  val repr: String :: Int :: Boolean :: HNil = iceCreamGen.to(iceCream)
  /** new instance created from a RAW data */
  val iceCream2: IceCream = iceCreamGen.from(repr)
  /** tuples out of the box as well */

  /**
    * coproducts
    */
  case class Red()
  case class Amber()
  case class Green()
  type Light = Red :+: Amber :+: Green :+: CNil // :+: === or
  
  val red: Light = Inl(Red())
  val green: Light = Inr(Inr(Inl(Green())))

  /** 2.3.1 Switching encodings using Generic */
  val gen = Generic[Shape]
  val r3 = gen.to(Rectangle(3.0, 4.0))
  val r4 = gen.to(Circle(1.0))
  
}
