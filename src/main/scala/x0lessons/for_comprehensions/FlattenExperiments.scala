package x0lessons.for_comprehensions

object FlattenExperiments extends App {
  val a = List( List(1,2), List(3,4) )
  val af = a.flatten
  println(af)
}
