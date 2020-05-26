package fp_red.red07.a2_nonblock

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{Callable, CountDownLatch, ExecutorService, Executors}

import fp_red.red07.a2_nonblock.Ch07step9nonblocking.Par

/**
  * non blocking version
  * but can be run in parallel
  */
object Ch07step9nonblocking extends App {

  // no changes to our type. but we will implement Future my ourselves
  type Par[+A] = ExecutorService => NBFuture[A]

  // that's no more than pass(register) callback
  trait NBFuture[+A] {
    private [a2_nonblock] def apply(k: A => Unit): Unit
  }

  object Par {
    // runner
    def run[A](es: ExecutorService)(par: Par[A]): A = {
      // A mutable, threadsafe reference, to use for storing the result
      val ref: AtomicReference[A] = new AtomicReference[A]
      // A latch which, when decremented, implies that `ref` has the result
      val latch: CountDownLatch = new CountDownLatch(1)

      // obtaining future instance
      val future: NBFuture[A] = par(es)
      // setting the action
      future { a => ref.set(a); latch.countDown() } // Asynchronously set the result, and decrement the latch
      // Block current thread until future will be done and latch will be released
      latch.await()
      // returning the value which was set via `ref.set(a)`
      ref.get
    }

    // unit implementation (actually callback registering)
    def unit[A](a: A): Par[A] = _ => new NBFuture[A] {
      def apply(cb: A => Unit): Unit = cb(a)
    }

    // lazy version of unit is the same, because by design, unit() and unitDelayed() both are lazy
    def unitDelayed[A](a: => A): Par[A] = _ => new NBFuture[A] {
      def apply(cb: A => Unit): Unit = cb(a)
    }

    // Helper function, for evaluating an action
    // asynchronously, using the given `ExecutorService`.
    // f: (es, r) => Unit. into `r` we need to write our code
    private def eval(es: ExecutorService)(callback: => Unit): Unit = {
      val _ = es.submit(new Callable[Unit] { def call: Unit = callback })
    }

    // Helper function for constructing `Par` values out of calls
    // to non-blocking continuation-passing-style APIs.
    // This will come in handy in Chapter 13.
    private def async[A](f: (A => Unit) => Unit): Par[A] = _ => new NBFuture[A] {
      def apply(k: A => Unit) = f(k)
    }

    // explicit forking
    def fork[A](a: => Par[A]): Par[A] = es => new NBFuture[A] {
      def apply(cb: A => Unit): Unit =
        eval(es)(  a(es)(cb)  )
      // if we use that approach it won't fork
//      a(es)(cb)
    }

    // map2 non-blocking implementation
    def map2[A,B,C](pa: Par[A], pb: Par[B])(f: (A,B) => C): Par[C] = es => new NBFuture[C] {
      def apply(cb: C => Unit): Unit = {
        println(s"t:map2:${Thread.currentThread().getName}")
        var ar: Option[A] = None
        var br: Option[B] = None
        val combiner = Actor[Either[A,B]](es) { (handler: Either[A,B]) => handler match {
          case Left(a) => {
//            println(s"Actor got Left($a)")
            if (br.isDefined)
              // joining result in another thread
              eval(es)(cb(f(a, br.get)))
              // joining result in the same thread
//              cb(f(a, br.get))
            else ar = Some(a)
          }
          case Right(b) => {
//            println(s"Actor got Right($b)")
            if (ar.isDefined)
            // joining result in another thread
              eval(es)(cb(f(ar.get, b)))
              // joining result in the same thread
//              cb(f(ar.get, b))
            else br = Some(b)
          }
        }}
        pa(es)(a => combiner ! Left(a))
        pb(es)(b => combiner ! Right(b))
      }
    }

    // specialized version of `map`
    def map[A,B](p: Par[A])(f: A => B): Par[B] = es => new NBFuture[B] {
      def apply(cb: B => Unit): Unit = p(es)(a => eval(es) { cb(f(a)) })
    }

    // education only
    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

    // education only
    def asyncF[A,B](f: A => B): A => Par[B] = a => lazyUnit(f(a))

    // education only
    def sequenceRight[A](as: List[Par[A]]): Par[List[A]] = as match {
      case Nil => unit(Nil)
      case h::t => map2(h, fork(sequence(t)))(_ :: _)
    }

    // education only
    def sequenceBalanced[A](as: IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork { as.length match {
      case 0 => unit(Vector())
      case 1 => map(as.head)(a => Vector(a))
      case _ => {
        val (l,r) = as.splitAt(as.length/2)
        map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
      }
    }}

    def sequence[A](as: List[Par[A]]): Par[List[A]] =
      map(sequenceBalanced(as.toIndexedSeq))(_.toList)

    // exercise answers

    /*
     * We can implement `choice` as a new primitive.
     *
     * `p(es)(result => ...)` for some `ExecutorService`, `es`, and
     * some `Par`, `p`, is the idiom for running `p`, and registering
     * a callback to be invoked when its result is available. The
     * result will be bound to `result` in the function passed to
     * `p(es)`.
     *
     * If you find this code difficult to follow, you may want to
     * write down the type of each subexpression and follow the types
     * through the implementation. What is the type of `p(es)`? What
     * about `t(es)`? What about `t(es)(cb)`?
     */
    // Par: ES => Future[A]
    // pCond(es), pTrue(es), pFalse(es) are NBFutures
    def choice[A](pCond: Par[Boolean])(pTrue: Par[A], pFalse: Par[A]): Par[A] = es => new NBFuture[A] {
      def apply(callback: A => Unit): Unit =
        pCond(es) { b =>
          if (b) eval(es) { pTrue (es)(callback) }
          else   eval(es) { pFalse(es)(callback) }
        }
    }

    // author version
    def choiceN1[A](p: Par[Int])(ps: List[Par[A]]): Par[A] = es => new NBFuture[A] {
      def apply(cb: A => Unit): Unit =
        p(es) { ind => eval(es) { ps(ind)(es)(cb) }}
    }

    // my version
    def choiceN[A](parIdx: Par[Int])(pars: List[Par[A]]): Par[A] = es => new NBFuture[A] {
      def apply(cb: A => Unit): Unit =
        parIdx(es) { idx =>
                  // pars(idx)(es) { cb }   // will run in the current thread
          eval(es) { pars(idx)(es) { cb } } // will be run in separate tread
        }
    }

    def choiceViaChoiceN1[A](pCond: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] = es => new NBFuture[A] {
      def apply(cb: A => Unit): Unit = pCond(es) { b => choiceN(unit(if (b) 0 else 1))(List(ifTrue, ifFalse)) }
    }

    def choiceViaChoiceN2[A](pCond: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] =
      choiceN(  map(pCond)(b => if (b) 0 else 1)  )(  List(ifTrue, ifFalse)  )

    def choiceMap[K,V](p: Par[K])(ps: Map[K,Par[V]]): Par[V] = es => new NBFuture[V] {
      def apply(cb: V => Unit): Unit = p(es) { k => ps(k)(es) { cb } }
    }
//      flatMap(p)(k => ps(k))

    // see `Nonblocking.scala` answers file. This function is usually called something else!
    def chooser[A,B](p: Par[A])(f: A => Par[B]): Par[B] = flatMap(p)(f)

    def flatMap[A,B](p: Par[A])(f: A => Par[B]): Par[B] = es => new NBFuture[B] {
      def apply(k: B => Unit): Unit = p(es) { a => f(a)(es)(k) }
    }

    def choiceViaChooser[A](p: Par[Boolean])(f: Par[A], t: Par[A]): Par[A] =
      chooser(p)(b => if (b) t else f)

    def choiceNChooser[A](p: Par[Int])(choices: List[Par[A]]): Par[A] =
      chooser(p)(i => choices(i))

    def join1[A](p: Par[Par[A]]): Par[A] =
      flatMap(p)(pa => pa)

    def join2[A](p: Par[Par[A]]): Par[A] = es => new NBFuture[A] {
      def apply(cb: A => Unit): Unit = p(es) { pa =>
        eval(es) { pa(es)(cb) }
      }
    }

    def joinViaFlatMap[A](a: Par[Par[A]]): Par[A] =
      flatMap(a)(x => x)

    def flatMapViaJoin[A,B](p: Par[A])(f: A => Par[B]): Par[B] =
      join1(map(p)(f))

//    /* Gives us infix syntax for `Par`. */
//    implicit def toParOps[A](p: Par[A]): ParOps[A] = new ParOps(p)
//
//    // infix versions of `map`, `map2`
//    class ParOps[A](p: Par[A]) {
//      def map[B](f: A => B): Par[B] = Par.map(p)(f)
//      def map2[B,C](b: Par[B])(f: (A,B) => C): Par[C] = Par.map2(p,b)(f)
//      def zip[B](b: Par[B]): Par[(A,B)] = p.map2(b)((_,_))
//    }
  }

  val join_parts: (Int, Int) => Int = (l: Int, r: Int) => {
    println(s"T:join:${Thread.currentThread().getName}")
    l + r
  }

  def sum(xs: IndexedSeq[Int]): Par[Int] = xs.length match {
    case 0 => Par.unit(0)
    case 1 => Par.unit(xs.head)
    case _ => {
      val (l, r) = xs.splitAt(xs.length/2)
      val sumL: Par[Int] = sum(l)
      val sumR: Par[Int] = sum(r)
      Par.map2(Par.fork(sumL), Par.fork(sumR))(join_parts)
//      Par.map2(sumL, sumR)(_ + _)
//      Par.map2(sumL, sumR)(join_parts)
    }
  }

  val data = IndexedSeq(1,2,3,4,5)
  val representation = sum(data)
  val es = Executors.newFixedThreadPool(10)
  val total: Int = Par.run(es)(representation)
  es.shutdown()
  printf(s"sum of `$data` is: $total")

}
