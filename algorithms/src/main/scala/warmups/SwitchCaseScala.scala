package warmups

object SwitchCaseScala extends App {

  def changeCase(s: String): String = s map { c => (c ^ 32).toChar }

  val orig = "aBcDefgHIJ"
  println(orig)
  println(changeCase(orig))
}
