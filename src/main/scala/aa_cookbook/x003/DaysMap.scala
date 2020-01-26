package aa_cookbook.x003

object DaysMap extends App {
  val days = Map(
    1 -> "Monday",
    2 -> "Tuesday",
    3 -> "Wednesday",
    4 -> "Thursday",
    5 -> "Friday",
    6 -> "Saturday",
    7 -> "Sunday",
  )
  val day = days(3)
  println(day)
  val dx = days.getOrElse(8, "not a day")
  println(dx)

  val x1 = 5 match {
    case 1 | 3 | 5 => "odd"
    case 2 | 4 | 6 => "even"
    case _ => "smth other"
  }
  println(x1)
}
