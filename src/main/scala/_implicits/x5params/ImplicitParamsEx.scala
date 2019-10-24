package _implicits.x5params

/**
  * Something missing: implicit parameters
  */
object ImplicitParamsEx extends App {

  def multiply1(x: Int)(implicit y: Int): Int = x * y

  val x1 = multiply1(4)(5) // 20
  val x2 = multiply1(4)(6) // 24

  // the name of the variable doesn't matter
  implicit val z: Int = 10
//  implicit val z2: Int = 11 // ambiguous
  val x3 = multiply1(4)(7) // 28
  val x4 = multiply1(4) // 40
}
