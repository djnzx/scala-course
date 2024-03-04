package maxgrowlist

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MaxGrowingListTest extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  /** foldLeft implementation */
  def findMaxGrowingList(xs0: List[Int]): List[Int] = xs0 match {
    case Nil     => Nil
    case x :: xs =>
      val s0 = (List.empty[Int], List(x), x)
      val (maxL, curL, _) = xs.foldLeft(s0) {
        case ((maxL, curL, prev), curr) if curr >= prev       => (maxL, curr :: curL, curr)
        case ((maxL, curL, _), curr) if curL.size > maxL.size => (curL, List(curr), curr)
        case ((maxL, _, _), curr)                             => (maxL, List(curr), curr)
      }
      (if (curL.size > maxL.size) curL else maxL).reverse
  }

  /** recursive implementation */
  def findMaxGrowingListR(xs: List[Int]): List[Int] = {

    def bigger(xs: List[Int], ys: List[Int]): List[Int] = if (xs.length > ys.length) xs else ys

    def go(xs: List[Int], maxL: List[Int], curL: List[Int], prev: Option[Int]): List[Int] = (xs, prev) match {
      case (Nil, _)                           => bigger(curL, maxL).reverse     // exit
      case (x :: xs, None)                    => go(xs, maxL, List(x), Some(x)) // start
      case (x :: xs, Some(prev)) if x >= prev => go(xs, maxL, x :: curL, Some(x))
      case (x :: xs, _)                       => go(xs, bigger(curL, maxL), List(x), Some(x))
    }

    go(xs, List.empty, List.empty, None)
  }

  test("1") {
    val data = Table(
      "in"                                                                                -> "out",
      //                                                   vvvvvvvvvvvvvvvvvvvvvvvvvvvvv
      List(1, 2, 3, 4, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 1, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11) -> List(1, 2, 3, 4, 5, 6, 7, 8, 9, 11),
      //               vvvvvvvvvvvvvvvvvvv
      List(1, 2, 3, 4, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 1, 2, 1, 2)                          -> List(1, 2, 3, 4, 5, 6, 7)
    )

    forAll(data) { (in, out) =>
      val actual = findMaxGrowingList(in)
      val actualR = findMaxGrowingListR(in)
      pprint.log(actual)
      pprint.log(actualR)
      actual shouldBe out
      actualR shouldBe out
    }
  }

}
