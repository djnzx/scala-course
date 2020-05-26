package fp_red.red07.a1_block

import java.util.concurrent._

import scala.language.implicitConversions

/**
  * implement map2, fork
  */
object Ch07step8 extends App {

  type Par[A] = ExecutorService => Future[A]

  case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def isDone: Boolean = true
    override def get(timeout: Long, unit: TimeUnit): A = get
  }

  object Par {
    /**
      * `unit` is represented as a function that returns a `UnitFuture`,
      * which is a simple implementation of `Future` that just wraps a constant value.
      * It doesn't use the `ExecutorService` at all.
      * It's always done and can't be cancelled.
      * Its `get` method simply returns the value that we gave it.
      */
    def unit[A](a: A): Par[A] = _ => UnitFuture(a)
    // it will create a UNIT of PARALLELISM based on value LAZILY
    def lazyUnit[A](a: => A): Par[A] = Par.fork(unit(a))

    /**
      * `map2` doesn't evaluate the call to `f` in a separate logical thread,
      * in accord with our design choice of having `fork` be the sole function in the API for controlling parallelism.
      * We can always do `fork(map2(a,b)(f))` if we want the evaluation of `f` to occur in a separate thread.
      */
    def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = es => {
      val l: Future[A] = pa(es)
      val r: Future[B] = pb(es)
      val sum = f(l.get, r.get)
      UnitFuture(sum)
    }
    def map[A,B](pa: Par[A])(f: A => B): Par[B] =
      map2(  pa, Par.unit(())   )(  (a,_) => f(a)  )

    /**
      * This is the simplest and most natural implementation of `fork`,
      * but there are some problems with it--for one,
      * the outer `Callable` will block waiting for the "inner" task to complete.
      * Since this blocking occupies a thread in our thread pool,
      * or whatever resource backs the `ExecutorService`,
      * this implies that we're losing out on some potential parallelism.
      * Essentially, we're using two threads when one should suffice.
      * This is a symptom of a more serious problem with the implementation,
      * and we will discuss this later in the chapter.
      */
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

    def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map(parList)(_ sorted)

    def equal[A](es: ExecutorService)(p1: Par[A], p2: Par[A]): Boolean =
      p1(es).get == p2(es).get

    def delay[A](pa: => Par[A]): Par[A] = es => pa(es)

    def choice[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] = es =>
      if (cond(es).get()) t(es) else f(es)

    implicit def toParOps[A](p: Par[A]): ParOps[A] = new ParOps(p)

    class ParOps[A](p: Par[A]) {
      /**
        * these methods will appear on Par instances
        */
      def m1(): Unit = println("method from implicit")
    }
  }

  import Par.toParOps

  def sum(xs: IndexedSeq[Int]): Par[Int] = xs.length match {
    case 0 => Par.unit(0)
    case 1 => Par.unit(xs.head)
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL: Par[Int] = sum(l)
      val sumR: Par[Int] = sum(r)
      sumL.m1()
      Par.map2(Par.fork(sumL), Par.fork(sumR))(_ + _)
    }
  }

  val data = IndexedSeq(1,2,3,4,5)
  val representation = sum(data)
  val es = Executors.newFixedThreadPool(10)
  val result: Future[Int] = Par.run(es)(representation)
  es.shutdown()
  printf(s"sum of `$data` is: ${result.get}")
}

object LawsApp extends App {
  import fp_red.red07.a1_block.Ch07step8.Par

  def equals[A](e: ExecutorService)(p1: Par[A], p2: Par[A]): Boolean =
    p1(e).get == p2(e).get

  val es = Executors.newFixedThreadPool(10)

  println(equals(es)(Par.unit(7), Par.fork(Par.unit(7))))

  es.shutdown()

}
