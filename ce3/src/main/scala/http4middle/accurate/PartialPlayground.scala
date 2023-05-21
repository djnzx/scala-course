package http4middle.accurate

object PartialPlayground {

  val f1: PartialFunction[Int, String] = { case 1 => "one" }

  val f2: Int => Option[String] = f1.lift

  val f3: PartialFunction[Int, String] = Function.unlift(f2)

}
