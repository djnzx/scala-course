package cookbook.x010

object C10_13 extends App {
  val fruits = List("apple", "banana", "orange")
  val f = for (i <- fruits.indices) yield (i, fruits(i))
  println(f)

  val f2 = for {
    f <- fruits
    if f.startsWith("a")
  } yield f

  print(f2)

}
