package catseffx.gabriel

/**
  * referential transparency problem
  */
object ReferentialTransparency extends App {
  val a = 123
  val t1 = (a, a)
  val t2 = (123, 123)
  println(t1==t2)

  val b = println("Hello")
  val p1 = (b, b)
  val p2 = (println("Hello"), println("Hello"))
  println(p1 == p2)
  // the same history with the Future...
}
