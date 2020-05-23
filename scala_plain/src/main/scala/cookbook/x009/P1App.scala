package cookbook.x009

object P1App extends App {
  val range = List.range(1, 10)
  val evens1 = range.filter((el: Int) => el % 2 == 0)
  val evens2 = range.filter(el => el % 2 == 0)
  val evens3 = range.filter(_ % 2 == 0)

  range.foreach(el => println(el))
  range.foreach(println(_))
  range.foreach(println)
}
