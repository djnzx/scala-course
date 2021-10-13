package a_interview

object Q1_flatMapBasics {

  val x = (1 to 10).filter(_ % 2 == 0)
  val f2 = (1 to 10).flatMap {
    case x if x % 2 == 0 => Some(x)
    case _               => None
  }
  println(x)
  println(f2)

}
