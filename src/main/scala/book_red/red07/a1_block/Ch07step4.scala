package book_red.red07.a1_block

object Ch07step4 extends App {

  /**
    * this Par - is description
    * of our computations
    */
  type Par[A]

  /**
   * creating fork and lazy
   * combining parallel computations
   */
  object Par {
    def unit[A](a: A): Par[A] = ???
    def lazyUnit[A](a: => A): Par[A] = Par.fork(unit(a))
    def get[A](pa: Par[A]): A = ???
    def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = ???
    def fork[A](a: => Par[A]): Par[A] = ???
    def run[A](a: Par[A]): A = ???
  }

  def sum(xs: List[Int]): Par[Int] = xs.length match {
    case 0 => Par.unit(0)
    case 1 => Par.unit(xs.head)
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL: Par[Int] = sum(l)
      val sumR: Par[Int] = sum(r)
      Par.map2(Par.fork(sumL), Par.fork(sumR))(_ + _)
    }
  }

  val data = List(1,2,3,4,5)
  val s = sum(data)
  printf(s"sum of `$data` is: $s")
}
