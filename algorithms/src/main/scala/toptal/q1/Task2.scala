package toptal.q1

import scala.annotation.tailrec

object Task2 extends App {

  def pack[A](xs: List[A]): List[(A, Int)] = {

    @tailrec
    def packIt(xs: List[A], tmp: Option[(A, Int)], acc: List[(A, Int)]): List[(A, Int)] = (xs, tmp) match {
      case (Nil, None)                           => acc
      case (Nil, Some(t @ _))                    => t :: acc
      case (xh :: xt, None)                      => packIt(xt, Some(xh, 1), acc)
      case (xh :: xt, Some((c, cnt))) if xh == c => packIt(xt, Some(c, cnt + 1), acc) // the same letter, keep counting
      case (xh :: xt, Some(t @ _))               => packIt(xt, Some(xh, 1), t :: acc) // the letter is different, start counting from 1
    }

    packIt(xs, None, Nil) reverse
  }

  def unpack1[A](xs: List[(A, Int)]): List[A] =
    xs.flatMap { case (a, n) => List.fill(n)(a) }

  def unpack2[A](xs: List[(A, Int)]): List[A] =
    xs.map { case (a, n) => List.fill(n)(a) }
      .flatten

  def unpack3[A](xs: List[(A, Int)]): List[A] =
    xs.map { case (a, n) => List.fill(n)(a) }
      .foldLeft(List.empty[A])(_ ++ _)

  def addToHead[A](a: A, n: Int, xs: List[A]): List[A] =
    n match {
      case 0 => xs
      case n => addToHead(a, n - 1, a :: xs)
    }

  def unpack4[A](xs: List[(A, Int)]): List[A] =
    xs.foldLeft(List.empty[A]) { case (as, (a, n)) =>
      addToHead(a, n, as)
    }

  def unpack5[A](xs: List[(A, Int)]): List[A] =
    for {
      x  <- xs
      xx <- List.fill(x._2)(x._1)
    } yield xx

  val a = "aabbbca".toList
  val p = pack(a)
  pprint.log(unpack1(p))
  pprint.log(unpack2(p))
  pprint.log(unpack3(p))
  pprint.log(unpack4(p))
  pprint.log(unpack5(p))

}
