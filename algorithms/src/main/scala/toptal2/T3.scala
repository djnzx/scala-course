package toptal2

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.reflect.ClassTag

object T30 {
  def zipWithInd(a: Array[Int]) = 
    a.lazyZip(LazyList from 1)
  
  def bi(n: Int) =
    n * (n - 1) / 2
}

/**
  * O(N) to get Qi
  * O(N) to convert it Map
  * O(N) to map to combinations
  * O(N) to sum
  */
object T3A {
  import T30._

  def numOfSubArraysWithMean(a: Array[Int], s: Int) =
    zipWithInd(a)
      .foldLeft((0, List(0))) { case ((pi, q), (ai, i)) =>
        val pi2 = pi + ai
        val qi = pi2 - s * i
        (pi2, qi::q)
      }._2
      .groupBy(identity)
      .withFilter { case (_, v) => v.length > 1 }
      .map { case (_, v) => bi(v.length) }
      .sum
}

/**
  * O(N) to get Qi
  * O(N) to convert it Map (and take length only)
  * O(N) to map to combinations
  * O(N) to sum
  */
object T3B {
  import T30._

  def numOfSubArraysWithMean(a: Array[Int], s: Int) =
    zipWithInd(a)
      .foldLeft((0, List(0))) { case ((pi, q), (ai, i)) =>
        val pi2 = pi + ai
        val qi = pi2 - s * i
        (pi2, qi::q)
      }._2
      .groupMapReduce(identity)(_=>1)(_+_)
      .withFilter { case (_, v) => v > 1 }
      .map { case (_, v) => bi(v) }
      .sum
}

/**
  * O(N) to get Qi and put them to Map
  * O(N) to map to combinations
  * O(N) to sum
  */
object T3C {
  import T30._
  
  def numOfSubArraysWithMean(a: Array[Int], s: Int): Int = {
    zipWithInd(a)
      .foldLeft((0, Map(0->1))) { case ((pi, q), (ai, i)) =>
        val pi2 = pi + ai
        val qi = pi2 - s * i
        (pi2, q.updatedWith(qi) {
          case None    => Some(1)
          case Some(x) => Some(x+1)
        })
      }._2
      .withFilter { case (_, v) => v > 1 }
      .map { case (_, v) => bi(v) }
      .sum
  }
}

/**
  * O(N) to get Qi and put them to Map
  * O(N) to map to combinations and reduce to sum in one pass
  */
object T3D {
  import T30._
  
  def numOfSubArraysWithMean(a: Array[Int], s: Int): Int =
    zipWithInd(a)
      .foldLeft((0, Map(0 -> 1))) { case ((pi, q), (ai, i)) =>
        val pi2 = pi + ai
        val qi = pi2 - s * i
        (pi2, q.updatedWith(qi) {
          case None => Some(1)
          case Some(x) => Some(x + 1)
        })
      }._2
      .foldLeft(0) { case (acc, (_, len)) =>
        if (len > 1) acc+bi(len) else acc
      }
}

class T3Spec extends AnyFunSpec with Matchers {
  import T3D._
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
      (a(0,4,3,-1),2) -> 2,
    )
    for {
      ((a, s), r) <- data
    } numOfSubArraysWithMean(a, s) shouldEqual r
  }
}