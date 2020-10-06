package toptal2

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.reflect.ClassTag

object T3 {
  def numOfSubArraysWithMean(a: Array[Int], s: Int): Int =
    a.lazyZip(LazyList from 1)
      .foldLeft((0, List(0))) { case ((pi, q), (ai, i)) =>
        val pi2 = pi + ai
        val qi = pi2 - s * i
        (pi2, qi :: q)
      }._2
      .groupMapReduce(identity)(_=>1)(_+_)
      .withFilter { case (_, v) => v > 1 }
      .map { case (_, v) => v * (v - 1) / 2 }
      .sum
}

class T3Spec extends AnyFunSpec with Matchers {
  import T3._
  def a[A: ClassTag](aa: A*): Array[A] = aa.toArray
  it("a") {
    val data = Seq(
      (a(5,3,6,2,5,5,2),4) -> 8,
      (a(5,3,6,2,5),4) -> 4,
      (a(5,3,6,2,5,3),4) -> 7,
      (a(5,3,6,2),4) -> 3,
      (a(5,3,6,2,4),4) -> 6,
      (a(2,1,3,7,2,2,1,3),2) -> 9,
      (a(0,8,1,7,2,6,3,5),4) -> 10,
      (a(2,2,3,4,1,1),2) -> 4,
      (a(2,2,2,2,2,2),2) -> 21,
      (a(2,2,2,2),2) -> 10,
    )
    for {
      ((a, s), r) <- data
    } numOfSubArraysWithMean(a, s) shouldEqual r
  }
}