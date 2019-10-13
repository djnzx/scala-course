package __udemy.scala_beginners.lectures.part3fp._set

object Mutable2SetApp extends App {
  // Understanding Mutable Variables with Immutable Collections
  var set = Set(1,2)
  set += 3
  println(set)
  set += 4
  println(set)
}
