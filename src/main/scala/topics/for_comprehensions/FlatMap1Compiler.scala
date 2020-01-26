package topics.for_comprehensions

object FlatMap1Compiler extends App {
  // for loop, one level
  val line1: Seq[Int] = for {
    i <- 1 to 5 // gonna be converted to .map
  } yield i
  val line2: Seq[Int] = 1 to 5 map(i => i)

  // for loop, two levels
  val square1: Seq[(Int, Int)] = for {
    i <- 1 to 3 // gonna be converted to .flatMap
    j <- 1 to 3 // gonna be converted to .map
  } yield (i, j)
  val square2: Seq[(Int, Int)] = 1 to 3 flatMap(i => 1 to 3 map(j => (i, j)))

  // for loop, nested
  val cube1: Seq[(Int, Int, Int)] = for {
    i <- 1 to 3 // gonna be converted to .flatMap
    j <- 1 to 3 // gonna be converted to .flatMap
    k <- 1 to 3 // gonna be converted to .map
  } yield (i, j, k)

  // will be compiled to under the hood for you
  val cube2: Seq[(Int, Int, Int)] = 1 to 3 flatMap(i => 1 to 3 flatMap(j => 1 to 3 map(k => (i, j, k))))
  //                                  i                  j                  k
}
