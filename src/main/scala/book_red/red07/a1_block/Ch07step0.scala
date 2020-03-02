package book_red.red07.a1_block

object Ch07step0 extends App {

  def sum(xs: List[Int]): Int =
    xs.foldLeft(0)(_ + _)

  val data = List(1,2,3,4,5)
  val s = sum(data)
  printf(s"sum of `$data` is: $s")
}
