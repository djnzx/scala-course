package ninetynine

import scala.annotation.tailrec

object P24 {
  // extracts ONE element at index from list to (el, rest)
  def extractAt[A](idx: Int, xs: List[A]): (A, List[A]) = {
    @tailrec
    def go(cnt: Int, xs: List[A], acc: List[A]): (A, List[A]) = xs match {
      case h::t => if (cnt < idx) go(cnt + 1, t, h::acc) else (h, acc.reverse ++ t)
      case _ => throw new NoSuchElementException
    }
    go(0, xs, Nil)
  }

  @tailrec
  def takeNrnd[A](xs: List[A], acc: List[A], cnt: Int): (List[A], List[A]) =
    if (acc.length == cnt) (acc, xs)
    else {
      val idx: Int = scala.util.Random.nextInt(xs.length)
      val (extracted:A, rest:List[A]) = extractAt[A](idx, xs)
      takeNrnd(rest, extracted::acc, cnt)
    }

  def lotto(cnt: Int, max: Int): (List[Int], List[Int]) = {
    val items: List[Int] = P22.range(1, max)
    println(items)
    takeNrnd(items, Nil, cnt)
  }

  def test(): Unit = {
    val lot = lotto(6,49)
    println(lot)
  }

}
