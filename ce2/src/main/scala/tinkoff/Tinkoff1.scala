package tinkoff

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec

/** 1) Cжать последовательность интов Seq(1, 2, 2, 3, 4, 3, 3, 3) => Seq((1, 1), (2, 2), (3, 1), (4, 1), (3, 3))
  *
  * Ответ выдать в виде Seq[(Int, Int)] (число из последовательности и число последовательных повторений) 2)
  * восстановаить исходную последовательность из сжатой
  */
object Tinkoff1 {

  def pack[A](xs: List[A]): List[(A, Int)] = {

    @tailrec
    def go(tail: List[A], cur: Option[(A, Int)], acc: List[(A, Int)]): List[(A, Int)] =
      (tail, cur) match {
        case (Nil, None)                        => acc
        case (Nil, Some(nc))                    => nc +: acc
        case (x :: t, None)                     => go(t, Some(x, 1), acc)
        case (x :: t, Some((n, cnt))) if x == n => go(t, Some(n, cnt + 1), acc)
        case (x :: t, Some(nc))                 => go(t, Some(x, 1), nc +: acc)
      }

    go(xs, None, List.empty) reverse
  }

  def unpack[A](xs: Seq[(A, Int)]): Seq[A] =
    xs.flatMap { case (n, c) => Seq.fill(c)(n) }

}

class Tinkoff1Spec extends AnyFunSpec with Matchers {

  val unpacked = List(1, 2, 2, 3, 4, 3, 3, 3)
  val packed = List((1, 1), (2, 2), (3, 1), (4, 1), (3, 3))

  import Tinkoff1._

  it("pack") {
    pack(unpacked) shouldEqual packed
  }

  it("unpack") {
    unpack(packed) shouldEqual unpacked
  }
}
