package book_red

import java.util.concurrent.{ExecutorService, Executors, Future, TimeUnit}

object Red007step5 extends App {

  type Par[A] = ExecutorService => Future[A]

  object Par {
    // our value of type 'A' representation
    private case class FutureUnit[A](get: A) extends Future[A] {
      override def cancel(mayInterruptIfRunning: Boolean): Boolean = false
      override def isCancelled: Boolean = false
      override def isDone: Boolean = true
      override def get(timeout: Long, unit: TimeUnit): A = get
    }
    // it will create a UNIT of PARALLELISM based on value
    def unit[A](a: A): Par[A] = ???
    // it will create a UNIT of PARALLELISM based on value LAZILY
    def lazyUnit[A](a: => A): Par[A] = Par.fork(unit(a))
    // we don't need extract the value, we need to run it
    def rum[A](es: ExecutorService)(pa: Par[A]): A = ???
    // mapper to avoid premature extraction
    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???
    // explicit forking to start evaluation immediately but in different threads
    def fork[A](pa: Par[A]): Par[A] = ???
  }

  // sum, divide and conquer approach
  def sum(xs: IndexedSeq[Int]): Par[Int] = xs.length match {
    case 0 => Par.unit(0)
    case 1 => Par.unit(xs.head)
    case _ => {
      // split as usual
      val (l, r) = xs.splitAt(xs.length/2)
      // create units
      val lr: Par[Int] = sum(l)
      // create units
      val rr: Par[Int] = sum(r)
      // mapping two computation into one + explicit forking
      Par.map2(Par.fork(lr), Par.fork(rr))(_ + _)
    }
  }

  val data = IndexedSeq(1,2,3,4,5)
  val representation = sum(data)
  val es = Executors.newFixedThreadPool(10)
  val result: Int = Par.rum(es)(representation)
  println(s"sum = $result")

}
