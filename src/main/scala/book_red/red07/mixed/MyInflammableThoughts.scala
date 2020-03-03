package book_red.red07.mixed

object Declaration {

  import java.util.concurrent.ExecutorService

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
    def run[A](es: ExecutorService)(pa: Par[M, A]): A
  }

}

object ImplementationBlocking {

  import Declaration._
  import java.util.concurrent.{ExecutorService, Future, TimeUnit}

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
    def run[A](es: ExecutorService)(pa: Par[Future, A]): A = pa(es).get
  }

}

object ImplementationNonBlocking {

  import Declaration._
  import java.util.concurrent.{ExecutorService, CountDownLatch, Callable}
  import java.util.concurrent.atomic.AtomicReference

  // that's no more than pass(register) callback
  trait NBFuture[+A] {
    def apply(cb: A => Unit): Unit
  }

  implicit object NonBlocking extends Parallel[NBFuture] {

    override def unit[A](a: A): Par[NBFuture, A] =
      _ => (cb: A => Unit) => cb(a)

    override def map2[A, B, C](pa: Par[NBFuture, A], pb: Par[NBFuture, B])(f: (A, B) => C): Par[NBFuture, C] = {
      println("map2: building")
      es =>
        new NBFuture[C] {
          def apply(cb: C => Unit): Unit = {
            println("map2: is running")
            println(s"t:map2:${Thread.currentThread().getName}")
            var ar: Option[A] = None
            var br: Option[B] = None
            val combiner = Actor[Either[A, B]](es) { (handler: Either[A, B]) => handler match {
                case Left(a) => {
                  println(s"got left:$a")
                  if (br.isDefined) eval(es) {
                    cb {
                      println("adding...")
                      f(a, br.get)
                    }
                  }
                  else ar = Some(a)
                }
                case Right(b) => {
                  println(s"got right:$b")
                  if (ar.isDefined) eval(es) {
                    cb {
                      println("adding...")
                      f(ar.get, b)
                    }
                  }
                  else br = Some(b)
                }
              }
            }
            pa(es) { a => combiner ! Left(a);  println(s"a sent to combiner: $a") }
            pb(es) { b => combiner ! Right(b); println(s"b sent to combiner: $b") }
          }
        }
    }

    override def fork[A](pa: => Par[NBFuture, A]): Par[NBFuture, A] = es => new NBFuture[A] {
      def apply(cb: A => Unit): Unit = eval(es) {
        pa(es) { cb } // dive into callback
      }
    }

    override def run[A](es: ExecutorService)(pa: Par[NBFuture, A]): A = {
      // A mutable, threadsafe reference, to use for storing the result
      val ref: AtomicReference[A] = new AtomicReference[A]
      // A latch which, when decremented, implies that `ref` has the result
      val latch: CountDownLatch = new CountDownLatch(1)

      // obtaining future instance
      val future: NBFuture[A] = pa(es)
      // setting the action
      future {
        println("2. starting algorithm. providing root callback a=ref.set(a)")
        a =>
          ref.set(a)
          latch.countDown()
          println(s"3.latch released")
      } // Asynchronously set the result, and decrement the latch
      // Block current thread until future will be done and latch will be released
      println(s"AWAITING for the end of the algorithm (actually can be printed anywhere)")
      latch.await()
      println("DONE")
      // returning the value which was set via `ref.set(a)`
      ref.get
    }

    private def eval(es: ExecutorService)(callback: => Unit): Unit = {
      val _ = es.submit(new Callable[Unit] { def call: Unit = callback })
    }
  }
}

object Code {

  import book_red.red07.mixed.Declaration._
  import java.util.concurrent.ExecutorService

  def sumr[M[_]](xs: IndexedSeq[Int])(implicit impl: Parallel[M]): Par[M, Int] = xs.length match {
    case 0 => impl.unit(0)
    case 1 => impl.unit(xs.head)
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL: Par[M, Int] = sumr[M](l)
      val sumR: Par[M, Int] = sumr[M](r)
      impl.map2(impl.fork(sumL), impl.fork(sumR))(_ + _)
    }
  }

  def sum[M[_]](xs: IndexedSeq[Int])(implicit impl: Parallel[M]): ExecutorService => Int = (es: ExecutorService) => {
    val f = sumr(xs)
    println("::RUNNING:impl.run(es)(sum(...))")
    impl.run(es)(f)
  }

}

object Application extends App {
  import java.util.concurrent.{Executors, ExecutorService}
  import ImplementationNonBlocking._
//  import ImplementationBlocking._

  val data: IndexedSeq[Int] = IndexedSeq(5)
  val representation: ExecutorService => Int = Code.sum(data)
  val es: ExecutorService = Executors.newFixedThreadPool(1)
  val result: Int = representation(es)
  es.shutdown()
  printf(s"sum of `$data` is: $result\n")

  //  val result2 = implicitly[Parallel[NBFuture]].run(es)(Code.sumr(data))
  //  val result3 = implicitly[Parallel[Future]].run(es)(Code.sumr(data))
  //  val result = implicitly[Parallel[Future]].run(es)(representation)
//  var rz = 0
//  NonBlocking.unit(7)(es)(x => rz = x)
//  println(rz)
}
