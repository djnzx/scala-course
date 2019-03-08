package x002

object Match extends App {
  val int = 100
  val s = int match {
    case 0 | 2 | 4 | 6  => "even"
    case 1 | 3 | 5 | 7  => "even"
//    case _ % 2 == 0 => "even big!"
    case _ => "other"
  }
  println(s)

}
