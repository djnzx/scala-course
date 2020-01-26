package topics.for_comprehensions

object FlatMap4Flatten extends App {
  val a = List( List(1,2), List(3,4) )
  val af = a.flatten
  println(af)
}
