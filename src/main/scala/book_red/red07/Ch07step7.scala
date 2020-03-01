package book_red.red07

import java.util.concurrent.{Callable, ExecutorService, Executors, Future, TimeUnit}

/**
  * implement map2, fork
  */
object Ch07step7 extends App {

  type Par[A] = ExecutorService => Future[A]

  case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def isDone: Boolean = true
    override def get(timeout: Long, unit: TimeUnit): A = get
  }

  object Par {
    // it will create a UNIT of PARALLELISM based on value
    def unit[A](a: A): Par[A] = _ => UnitFuture(a)
    // it will create a UNIT of PARALLELISM based on value LAZILY
    def lazyUnit[A](a: => A): Par[A] = Par.fork(unit(a))
    // that is actually implementation for `map2`
    def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = es => {
      val l = pa(es).get
      val r = pb(es).get
      val sum = f(l, r)
      UnitFuture(sum)
    }
    def fork[A](pa: => Par[A]): Par[A] = es => {
      println(s"fork...${Thread.currentThread.getName}")
      es.submit(new Callable[A] {
        override def call(): A = pa(es).get
      })
    }

    // we renamed get into run, because since now, we won't .get anytime
    // we will use .run to run our algorithm built
    //def get[A](pa: Par[A]): A = ???
    def run[A](es: ExecutorService)(pa: Par[A]): Future[A] = pa(es)
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
  val representation = sum(data)
  val es = Executors.newFixedThreadPool(10)
  val result: Future[Int] = Par.run(es)(representation)
  es.shutdown()
  printf(s"sum of `$data` is: ${result.get}")
}
