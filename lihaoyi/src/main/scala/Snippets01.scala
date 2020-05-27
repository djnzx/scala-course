object Snippets01 extends App {
  val s: String = java.lang.Integer.toBinaryString(65)
  val nzeroes = java.lang.Integer.numberOfLeadingZeros(1)
//  println("0"*nzeroes + s)
  println(nzeroes)

  // string pattern matching!
  def getDayMonthYear(s: String) = s match {
    case s"$day-$month-$year" => println(s"found day: $day, month: $month, year: $year")
    case _ => println("not a date")
  }

  case class Point(x: Int, y: Int)
  val p = Point(123, 456)
  val Point(x, y) = p
  println(x)
  println(y)

}
