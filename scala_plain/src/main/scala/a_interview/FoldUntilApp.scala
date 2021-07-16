package a_interview

import scala.annotation.tailrec

object FoldUntilApp extends App {

  /**
    * foldLeft implementation which allows
    * early traverse termination based on the predicate given
    * @param xs Iterator/Iterable[A]
    * @param z base accumulator value
    * @param f function to combine accumulator with the next value
    * @param t function to describe termination condition
    * @tparam A any type for the source collection
    * @tparam B any type for the accumulator
    * @return Tuple2[accumulator, rest of the iterator
    */
  def foldUntil[A, B](xs: Iterable[A])(z: B)(f: (B, A) => B, t: B => Boolean): (B, Iterable[A]) = {
    val it = xs.iterator

    foldUntil(it)(z)(f, t) match {
      case (b, _) => (b, it.to(Iterable))
    }
  }

  def foldUntil[A, B](it: Iterator[A])(z: B)(f: (B, A) => B, t: B => Boolean): (B, Iterator[A]) = {

    @tailrec
    def go(b: B): B = {
      /** iterator is exhausted */
      if (it.isEmpty) return b
      /** predicate shortcuts traversing */
      if (t(b)) return b
      /** process the tail */
      val nextA: A = it.next()
      val newB = f(b, nextA)
      go(newB)
    }

    (go(z), it)
  }

  val seq: Seq[Int] = 1 to 100
  println(seq)

  val (sum, tail) = foldUntil(seq)(0)(_ + _, _ >= 10)
  println(sum)
  println(tail)

}
