package fp_red.red07.a1_block_steps

object Ch07step1 extends App {

  def sum(xs: List[Int]): Int = xs.length match {
    case 0 => 0
    case 1 => xs.head
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      sum(l) + sum(r)
    }
  }

  val data = List(1,2,3,4,5)
  val s = sum(data)
  printf(s"sum of `$data` is: $s")
}
