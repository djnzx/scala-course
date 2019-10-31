package x00topics.implicits.i2conversion

object Application02 extends App {

  // function takes STRING ONLY
  def alert(msg: String): Unit = println(msg)

  // the name of the function/method doesn't matter
  implicit val intToString1: Int => String = (i: Int) => i.toString

  // compiler is looking for something useful with implicit declaration for convert int->string
  alert(42)
}
