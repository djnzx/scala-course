package cookbook.x003

import scala.annotation.switch

class SwitchDemo {
  val i = 1
  val x = (i: @switch) match {
    case 1  => "One"
    case 2  => "Two"
    case _  => "Other"
  }
}
