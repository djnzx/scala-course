package tkf

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.annotation.tailrec

object Tkf1 {

  /** tail recursive */
  def compress1[A](xs: List[A]): List[(A, Int)] = {

    @tailrec
    def go(tail: List[A], cur: Option[(A, Int)], acc: List[(A, Int)]): List[(A, Int)] =
      (tail, cur) match {
        case (Nil, None)                        => acc
        case (Nil, Some(nc))                    => nc +: acc
        case (x :: t, None)                     => go(t, Some(x, 1), acc)
        case (x :: t, Some((n, cnt))) if x == n => go(t, Some(n, cnt + 1), acc)
        case (x :: t, Some(nc))                 => go(t, Some(x, 1), nc +: acc)
      }

    go(xs, None, List.empty).reverse
  }

  def compress2[A](xs: List[A]): List[(A, Int)] =
    xs.foldLeft(List.empty[(A, Int)]) { (ps, a) =>
      ps.headOption match {
        case Some((`a`, c)) => (a -> (c + 1)) :: ps.tail
        case Some(_)        => (a -> 1) :: ps
        case None           => List(a -> 1)
      }
    }.reverse

  def compress3[A](xs: List[A]): List[(A, Int)] = xs
    .foldLeft(List.empty[(A, Int)]) {
      case (Nil, a)                     => List(a -> 1)
      case ((ac, c) :: t, a) if ac == a => (a -> (c + 1)) :: t
      case (pp, a)                      => (a -> 1) :: pp
    }
    .reverse

  def compress[A](xs: List[A]): List[(A, Int)] = xs
    .foldLeft(List.empty[(A, Int)]) { (pp, a) =>
      pp match {
        case (`a`, c) :: t => (a -> (c + 1)) :: t
        case _             => (a -> 1) :: pp
      }
    }
    .reverse

  def decompress[A](xs: Seq[(A, Int)]): Seq[A] =
    xs.flatMap { case (a, n) => Seq.fill(n)(a) }

}

class Tkf1 extends AnyFunSpec with Matchers {

  val raw = "abbcccaabc".toList
  val zip = List(('a', 1), ('b', 2), ('c', 3), ('a', 2), ('b', 1), ('c', 1))

  import Tkf1._

  it("pack") {
    compress(raw) shouldBe zip
  }

  it("unpack") {
    decompress(zip) shouldBe raw
  }

}
