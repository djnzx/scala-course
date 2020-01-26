package udemy.scala_beginners.lectures.part3fp._set

object MutableSetApp extends App {
  // mutable
  var set = scala.collection.mutable.Set[Int]()
  set += 1
  set += (2, 3)
  // add elements from any sequence (any TraversableOnce)
  set ++= Vector(44, 55)
  println(set)
  var r1 = set.add(1) // false, not added
  println(s"add 1: $r1")
  var r99 = set.add(99) // true, added
  println(s"add 99: $r99")
  var c99 = set.contains(99) // true
  println(s"contains(99): $c99")
  var c98 = set.contains(98) // false
  println(s"contains(98): $c98")

  var grouped1 = set.groupBy(el => el % 3)
  var grouped2 = set.groupBy(_>10)
  println(grouped1)
  println(grouped2)

  val setz = set.zip(Stream from 1) // set is unordered !
  println(setz)

  set -= 1
  println(set)
  set --= Vector(2, 3)
  println(set)

  set.isEmpty
  set.nonEmpty
}
