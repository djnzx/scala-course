package twitter

object PartialFunctionApp extends App {

  val sample = -9 to 9

  val isGt5:  PartialFunction[Int, String] = { case x if x > 5  => s"X=$x :GT5" }
  val isGt0:  PartialFunction[Int, String] = { case x if x > 0  => s"X=$x :GT0" }
  val isLtm5: PartialFunction[Int, String] = { case x if x < -5 => s"X=$x :LT-5"}
  val isLt0:  PartialFunction[Int, String] = { case x if x < 0  => s"X=$x :LT0" }
  val isZero: PartialFunction[Int, String] = { case x if x == 0 => s"X=$x :IS0" }
  val isElse: PartialFunction[Int, String] = { case x           => s"X=$x :ELSE"}

  val totalHandler =
    isGt5   // >5
      .orElse(isLtm5) // <-5
      .orElse(isGt0)  // >0
      .orElse(isLt0)  // <0
      .orElse(isZero) // =0
      .orElse(isElse)

  val r: Seq[String] = sample collect totalHandler
  println(r)

}
