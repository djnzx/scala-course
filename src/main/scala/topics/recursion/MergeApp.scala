package topics.recursion

import scala.annotation.tailrec
import Ordering.Implicits._

object MergeApp extends App {

  def merge[A: Numeric](left: Seq[A], right: Seq[A]): Seq[A] = mergeR(left, right, Nil)

  @tailrec
  def mergeR[A: Numeric](left: Seq[A], right: Seq[A], acc: Seq[A]): Seq[A] =
    (left, right) match {
      case (Nil, Nil) => acc
      case (Nil, _)   => acc :+ right
      case (_, Nil)   => acc :+ left
      case (lh::lt, rh::rt) => if (lh < rh) mergeR(lt, right, acc :+ lh)
                               else mergeR(left, rt, acc :+ rh)
    }

  assert(merge[Int](Nil, Nil) == Nil)
  assert(merge(Seq(1,2,3), Nil) == Seq(1,2,3))
  assert(merge(Nil, Seq(4,5,6)) == Seq(4,5,6))
  assert(merge(Seq(1,2,3), Seq(4,5,6)) == List(1, 2, 3, 4, 5, 6))
  assert(merge(Seq(1,3,5), Seq(2,4,6)) == List(1, 2, 3, 4, 5, 6))
  assert(merge(Seq(10,11,20), Seq(12,13,21)) == List(10, 11, 12, 13, 20, 21))
}
