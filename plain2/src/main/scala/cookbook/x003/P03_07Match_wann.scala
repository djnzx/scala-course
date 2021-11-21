package cookbook.x003

import scala.annotation.switch

object P03_07Match_wann extends App {
  val dayOfWeek = (d: Int) => {
    (d: @switch) match {
      case 1 => "Monday"
//      case t @ _ => "Tuesday"
      case 3 => "Wednesday"
      case 4 => "Thursday"
      case 5 => "Friday"
      case 6 => "Saturday"
      case 7 => "Sunday"
      case _ => "not a day ;("
    }
  }

  println(dayOfWeek(1))
  println(dayOfWeek(8))
}
