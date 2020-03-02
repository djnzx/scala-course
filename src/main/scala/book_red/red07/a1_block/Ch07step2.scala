package book_red.red07.a1_block

object Ch07step2 extends App {

  type Par[A]

  /**
   * create wrappers and extractors
   */
  object Par {
    def unit[A](a: A): Par[A] = ???
    def get[A](pa: Par[A]): A = ???
  }

  def sum(xs: List[Int]): Int = xs.length match {
    case 0 => 0
    case 1 => xs.head
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL: Par[Int] = Par.unit(sum(l))
      val sumR: Par[Int] = Par.unit(sum(r))
      // the problem is here.
      Par.get(sumL) + Par.get(sumR)
    }
  }

  val data = List(1,2,3,4,5)
  val s = sum(data)
  printf(s"sum of `$data` is: $s")
}
