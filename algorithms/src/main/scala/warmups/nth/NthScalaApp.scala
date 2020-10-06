package warmups.nth

object NthScalaApp extends App {
  def fmt_1(n: Int): String = n match {
    case 1 => "%d-st"
    case 2 => "%d-nd"
    case 3 => "%d-rd"
    case _ => "%d-th"
  }
  val fmt_2 = " number is:%d"
  val m = scala.io.StdIn.readLine("Enter the number:").toInt
  val r = new NthScala().calculate(m)
  print(m.formatted(fmt_1(m)))
  println(r.formatted(fmt_2))
}
