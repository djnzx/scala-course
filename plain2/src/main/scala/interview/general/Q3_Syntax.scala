package interview.general

object Q3_Syntax extends App {

  implicit class IntWithOptionSyntax(x: Int) {
    def +(yo: Option[Int]): Int =
      yo match {
        case Some(y) => x + y
        case None    => x
      }
  }

  val x: Int = 10 + Some(33)
  println(x)

}
