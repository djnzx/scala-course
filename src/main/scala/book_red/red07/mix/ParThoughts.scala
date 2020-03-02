package book_red.red07.mix

import java.util.concurrent.{ExecutorService, Executors, Future, TimeUnit}

object ParThoughts {
  /**
    * it is going to be function
    * ExecutorService => Future[A]
    * or
    * ExecutorService => NBFuture[A]
    */
  type Par[M[_], A] = ExecutorService => M[A]

  trait Parallel[M[_]] {
    def unit[A](a: A): Par[M,A]
    def map2[A, B, C](pa: Par[M, A], pb: Par[M, B])(f: (A, B) => C): Par[M, C]
    def fork[A](pa: => Par[M, A]): Par[M, A]
    def run[A](es: ExecutorService)(pa: Par[M, A]): M[A]
  }

  case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def isDone: Boolean = true
    override def get(timeout: Long, unit: TimeUnit): A = get
  }

  implicit object Blocking extends Parallel[Future] {
    def unit[A](a: A): Par[Future,A] = _ => UnitFuture(a)
    def map2[A, B, C](pa: Par[Future, A], pb: Par[Future, B])(f: (A, B) => C): Par[Future, C] = es => {
      val fa: Future[A] = pa(es)
      val fb: Future[B] = pb(es)
      val sum = f(fa.get, fb.get)
      UnitFuture(sum)
    }
    def fork[A](pa: => Par[Future, A]): Par[Future, A] = es => es.submit(() => pa(es).get)
    def run[A](es: ExecutorService)(pa: Par[Future, A]): Future[A] = pa(es)
  }

}

object BlockingExampleApp extends App {

  import ParThoughts._

  def sum[M[_]](xs: IndexedSeq[Int])(implicit impl: Parallel[M]): Par[M, Int] = xs.length match {
    case 0 => impl.unit(0)
    case 1 => impl.unit(xs.head)
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL = sum[M](l)
      val sumR = sum[M](r)
      impl.map2(impl.fork(sumL), impl.fork(sumR))(_ + _)
    }
  }

  val data = IndexedSeq(1,2,3,4,5)
  val representation = sum[Future](data)
  val es = Executors.newFixedThreadPool(10)
  val result: Future[Int] = Blocking.run(es)(representation)
  es.shutdown()
  printf(s"sum of `$data` is: ${result.get}")
}
