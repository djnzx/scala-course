package fp_red.red07.a1_block_steps

object Ch07step3 extends App {

  /**
    * this Par - is description
    * of our computations
    */
  type Par[A]

  /**
   * creating map2
   * combining parallel computations
   */
  object Par {
    def unit[A](a: A): Par[A] = ???
    def get[A](pa: Par[A]): A = ???
    def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = ???
  }

  def sum(xs: List[Int]): Par[Int] = xs.length match {
    case 0 => Par.unit(0)
    case 1 => Par.unit(xs.head)
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL: Par[Int] = sum(l)
      val sumR: Par[Int] = sum(r)
      Par.map2(sumL, sumR)(_ + _)
    }
  }

  val data = List(1,2,3,4,5)
  val s = sum(data)
  printf(s"sum of `$data` is: $s")
}
