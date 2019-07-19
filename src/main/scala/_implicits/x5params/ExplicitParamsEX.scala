package _implicits.x5params

object ExplicitParamsEX extends App {

  def multiply1(x: Int)(implicit y: Int): Int = x * y

  val x1 = multiply1(4)(5) // 20
  val x2 = multiply1(4)(6) // 24

  implicit val z: Int = 10
//  implicit val z2: Int = 11 // ambiguous
  val x3 = multiply1(4) // 40
}
