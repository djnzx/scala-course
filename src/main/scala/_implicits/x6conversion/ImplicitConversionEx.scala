package _implicits.x6conversion

object ImplicitConversionEx extends App {
  def alert(msg: String): Unit = println(msg)

//  implicit def intToString(i: Int): String = i.toString
//  implicit val intToString2 = (i: Int) => i.toString
  // the name of the function/method doesn't matter
  implicit val intToString1: Int => String = (i: Int) => i.toString

  // compiler is looking for something useful with implicit declaration for convert int->string
  alert(42)
}
