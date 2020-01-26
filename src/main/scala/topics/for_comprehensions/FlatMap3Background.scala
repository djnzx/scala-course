package topics.for_comprehensions

object FlatMap3Background extends App {
  val fruits: Seq[String] = Seq("apple", "banana", "orange")

  println(fruits)
  val fruits2: Seq[String] = fruits.map(_.toUpperCase)
  println(fruits2)

  // map + flatten
  val fruits3: Seq[Char] = fruits.flatMap(_.toUpperCase)
  println(fruits3)
  // it works because string in also iterable :)

  // this is the initial intention !
  val fruits4: Seq[String] = fruits.flatMap(x => Seq(x.toUpperCase))
  println(fruits4)

  val fruits5: Seq[Char] = fruits.flatten
  println(fruits5)
}
