package fp_red.red07.a1_block

import java.util.concurrent.{ExecutorService, Future, TimeUnit}

object Ch07step5 extends App {

  type Par[A] = ExecutorService => Future[A]

  /**
   * representation
   */
  case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def isDone: Boolean = true
    override def get(timeout: Long, unit: TimeUnit): A = get
  }

  object Par {
    def unit[A](a: A): Par[A] = ???
    def lazyUnit[A](a: => A): Par[A] = Par.fork(unit(a))
    //def get[A](pa: Par[A]): A = ???
    def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = ???
    def fork[A](a: => Par[A]): Par[A] = ???
    def run[A](es: ExecutorService)(a: Par[A]): A = ???
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
