package book_red

import java.util.concurrent.{ExecutorService, Executors, Future, TimeUnit}

object Red0071Idea extends App {
  // that's our Future[A] representation
  case class FutureUnit[A](a: A) extends Future[A] {
    override def cancel(evenIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def isDone: Boolean = true
    override def get(): A = a
    override def get(timeout: Long, unit: TimeUnit): A = a
  }

  // that's our desired calculation representation
  type Parallel[A] = ExecutorService => Future[A]

  // lift function  A => Future[A]
  def unit[A]    (a: A): Parallel[A] = _ => FutureUnit(a)

  // naive implementation
  def map2[A, B, C](a: Parallel[A], b: Parallel[B])(f: (A, B) => C): Parallel[C] = (es) =>
    FutureUnit(f(run(a)(es), run(b)(es)))

  def fork[A](a: => Parallel[A]): Parallel[A] = (es) => es.submit(() => a(es).get)

  def sumPar(ints: IndexedSeq[Int]): Parallel[Int] = {
    ints.length match {
      case 0 => unit(0)
      case 1 => unit(ints.head)
      case _ => ints.splitAt(ints.length / 2) match {
        case (l, r) => map2( fork(sumPar(l)), fork(sumPar(r)) )( _ + _ )
      }
    }
  }
  def sortPar(ints: Parallel[List[Int]]): Parallel[List[Int]] =
    map2(ints, unit(()))((a, _) => a.sorted)

  def run[A](rep: Parallel[A])(es: ExecutorService): A = rep(es).get

  val data = Vector(1,2,3,4,5,6)
  val sum_representation: Parallel[Int] = sumPar(data)
  val es: ExecutorService = Executors.newFixedThreadPool(10)
  val sum = run(sum_representation)(es)
  println(sum)
  es.shutdown()
}
