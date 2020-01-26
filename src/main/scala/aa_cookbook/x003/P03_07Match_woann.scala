package aa_cookbook.x003

object P03_07Match_woann extends App {
  val dayOfWeek = (d: Int) => d match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
    case _ => "not a day ;("
  }

  println(dayOfWeek(1))
  println(dayOfWeek(8))
}
