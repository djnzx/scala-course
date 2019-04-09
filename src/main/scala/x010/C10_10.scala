package x010

object C10_10 extends App {
  val fruits = List("apple", "banana", "orange")
  for (f <- fruits) println(f)

  for (i <- 0 until fruits.size) println(fruits(i))

  for (i <- fruits.indices) println(fruits(i))

  for ((elem, index) <- fruits.zipWithIndex) {
    println(s"$index: $elem")
  }

  for ((elem, index) <- fruits.zip(Stream from 1)) {
    println(s"$index: $elem")
  }

  val fruits_upper = for (i <- fruits.indices) yield fruits(i).toUpperCase
  println(fruits_upper)

  val mm = Map(1 -> "Jan", 2 -> "Feb", 3 -> "Mar")
  for ((k, v) <- mm) {
    println(s"$k:$v")
  }

  println(fruits_upper.zipWithIndex)

}
