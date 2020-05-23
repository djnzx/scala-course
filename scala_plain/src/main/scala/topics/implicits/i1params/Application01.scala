package topics.implicits.i1params

object Application01 extends App {
  // function with implicit parameter
  def multiply1(x: Int)(implicit y: Int): Int = x * y
  //                       ^         ^_ type

  val x1 = multiply1(4)(5) // 20
  val x2 = multiply1(4)(6) // 24

  // implicit variable declaration
  implicit val z371: Int = 10
  //    ^      the name of the variable doesn't matter. type DOES matter

//  implicit val z2: Int = 11 // ambiguity isn't allowed
  val x28 = multiply1(4)(7) // 28
  val x40 = multiply1(4) // 40
}
