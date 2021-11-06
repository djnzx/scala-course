package cookbook.x005

object TuplesCodeGenerator extends App {
  val tupadd = for (n <- 2 to 20) yield {
    val t = (0 until n).map(i => ('A'+i).toChar).mkString(", ")
    val u = ('A'+n).toChar
    val i = (0 until n).map(i => s"x._${i+1}").mkString(", ")
    List(
      s"implicit class TupOps$n[$t](val x: ($t)) extends AnyVal {",
      s"  def :+[$u](y: $u) = ($i, y)",
      s"  def +:[$u](y: $u) = (y, $i)",
      "}"
    ).mkString("\n")
  }
  println(tupadd.mkString("\n"))

}
