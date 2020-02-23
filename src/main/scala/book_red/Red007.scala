package book_red

import java.util.concurrent.{ExecutorService, Executors, Future}

import scala.concurrent.duration.TimeUnit

object Red007 extends App {
  val data = Vector(1,2,3,4,5,6)

  type Par[A] = ExecutorService => Future[A]

  object Par {
    private case class UnitFuture[A](get: A) extends Future[A] {
      def isDone = true
      def get(timeout: Long, units: TimeUnit): A = get
      def isCancelled = false
      def cancel(evenIfRunning: Boolean): Boolean = false
    }

    def unit[A]    (a:    A): Par[A] = _ => UnitFuture(a)
//    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

    def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] = (es: ExecutorService) => {
      println("map2:1")
      val af: Future[A] = a(es)
      println("map2:2")
      val bf: Future[B] = b(es)
      println("map2:3")
      UnitFuture(f(af.get, bf.get))
    }
    def fork[A](a: => Par[A]): Par[A] = (es: ExecutorService) => es.submit(() => a(es).get())
//    def run[A](es: ExecutorService)(a: Par[A]): Future[A] = a(es)
  }

  def sumPar(ints: IndexedSeq[Int]): Par[Int] = {
    if (ints.length <= 1) {
      println(s"ints:$ints")
      Par.unit(ints.headOption getOrElse 0)
    } else {
      val (l, r) = ints.splitAt(ints.length / 2)
      println(s"ints:$ints split to: l=$l, r=$r")
      Par.map2(Par.fork(sumPar(l)), Par.fork(sumPar(r)))((a, b) => a + b)
    }
  }

  val total: Par[Int] = sumPar(data)
  val es = Executors.newFixedThreadPool(1)
  val z = total(es).get()
  println(z)
  es.shutdown()
}
