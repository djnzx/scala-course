package misc

object ShapeApp extends App {

  sealed trait Shape
  case class Circle(radius: Int) extends Shape
  case class Square(side: Int) extends Shape
  case class Rectangle(width: Int, height: Int) extends Shape

  def area(s: Shape): Double = s match {
    case c: Circle    => Math.pow(c.radius, 2) * Math.PI
    case s: Square    => Math.pow(s.side, 2)
    case r: Rectangle => r.width * r.height
    case _            => -1
  }

  val rect45 = Rectangle(4, 5)
  val rect46 = rect45.copy(height = 6)

  println( area( Circle(5) ) )
  println( area( Square(4) ) )
  println( area( Rectangle(4, 5) ) )
  println( area( null ) )

  println( Circle(24/2) equals Circle(6*2))

  def add(x: Int)(y: Int) = x + y
  val add1 = add(1)(_)

  println( add(1)(2) )
  println( add1(3) )
  println( add1(4) )




}
