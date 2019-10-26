package x0lessons.for_comprehensions

object FlatMap1 extends App {

  // 1. minimal loop without outcome, just unit
  for (
    i <- 1 to 3
  ) print(s"$i ")
  println
  // under the hood converted to
  1 to 3 foreach(x => print(s"$x "))
  println

  // 2. minimal loop, produces sequence 1 to 4
  val iter11: Seq[Int] = for {
    i <- 1 to 4
  } yield i // Vector(1, 2, 3, 4)
  // under the hood converted to
  val iter12 = 1 to 4 map(x => x)
  println(iter11)
  println(iter12)

  // 3. the same but with 'if' clause
  val iter21: Seq[Int] = for {
    i <- 1 to 10 // gonna be converted to .map
    if i%2 == 0  // gonna be converted to .withFilter
  } yield i
  // under the hood converted to
  val iter22: Seq[Int] = 1 to 10 withFilter(_%2==0) map(x => x)

  // 4. nested loops

  val iter31: Seq[(Int, Int, Int)] = for {
    i <- 1 to 3 // gonna be converted to .flatMap
    j <- 1 to 3 // gonna be converted to .flatMap
    k <- 1 to 3 // gonna be converted to .map
  } yield (i, j, k)
  println(iter31)
  // under the hood converted to
  //              i
//  val iter32 = (1 to 3).flatMap(i => i, 1 to 3)

}
