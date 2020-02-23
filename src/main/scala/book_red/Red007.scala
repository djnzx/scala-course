package book_red

import java.util.concurrent.{ExecutorService, Executors, Future, TimeUnit}

object Red007 extends App {
  case class FutureUnit[A](a: A) extends Future[A] {
    override def cancel(evenIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def isDone: Boolean = true
    override def get(): A = a
    override def get(timeout: Long, unit: TimeUnit): A = a
  }

  type Par[A] = ExecutorService => Future[A]

  def unit[A]    (a: A): Par[A] = _ => FutureUnit(a)
//  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] = (es: ExecutorService) => {
    val af: Future[A] = a(es)
    val bf: Future[B] = b(es)
    FutureUnit(f(af.get, bf.get))
  }
  def fork[A](a: => Par[A]): Par[A] = (es: ExecutorService) => es.submit(() => a(es).get())

  def sumPar(ints: IndexedSeq[Int]): Par[Int] = {
    if (ints.length <= 1) {
      println(s"ints:$ints")
      unit(ints.headOption getOrElse 0)
    } else {
      val (l, r) = ints.splitAt(ints.length / 2)
      println(s"ints:$ints split to: l=$l, r=$r")
      map2(
        fork(sumPar(l)),
        fork(sumPar(r))
      )((a, b) => a + b)
    }
  }

  val data = Vector(1,2,3,4,5,6)
  val sum_representation: Par[Int] = sumPar(data)
  val es = Executors.newFixedThreadPool(10)
  val sum = sum_representation(es).get()
  println(sum)
  es.shutdown()
}
