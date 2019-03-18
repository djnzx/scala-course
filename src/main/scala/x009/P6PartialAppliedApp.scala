package x009

object P6PartialAppliedApp extends App {
  val sum = (a: Int, b: Int, c: Int) => a + b + c
  println(sum(1, 2, 3))
  val f = sum(1, 2, _)
  println(f(3))


}
