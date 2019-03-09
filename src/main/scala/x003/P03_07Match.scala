package x003

object P03_07Match extends App {
  val i = 5
  i match {
    case 1 => println("Monday")
    case 2 => println("Tuesday")
    case 3 => println("Wednesday")
    case 4 => println("Thursday")
    case 5 => println("Friday")
    case 6 => println("Saturday")
    case 6 => println("Sunday")
    case _ => println("not a day ;(")
  }
}
