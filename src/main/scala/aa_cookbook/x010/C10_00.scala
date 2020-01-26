package aa_cookbook.x010

object C10_00 extends App {
  val list = List.range(1,10)
  val evens = list.filter(_ % 2 == 0)
  val double = evens.map(_ * 2)

  println(list)
  println(evens)
  println(double)

  val same = for {
    e <- list
    if e % 2 ==0
  } yield e

  println(same)

}
