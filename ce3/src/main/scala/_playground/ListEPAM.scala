package _playground

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ListEPAM extends AnyFunSuite with Matchers {

  def swapPairs1(xs: List[Int]): List[Int] =
    xs.grouped(2)
      .flatMap {
        case List(x, y) => List(y, x)
        case xs         => xs
      }
      .toList

  def swapPairs2(xs: List[Int]): List[Int] = {

    def go(xs: List[Int], acc: List[Int]): List[Int] =
      xs match {
        case a :: b :: tail => go(tail, a :: b :: acc)
        case a :: tail      => go(tail, a :: acc)
        case _              => acc
      }

    go(xs, List.empty).reverse
  }

  test("1") {
    val swapPairs = swapPairs2 _
//    val swapPairs = swapPairs1 _
    swapPairs(List(1, 2, 3, 4, 5)) shouldBe List(2, 1, 4, 3, 5)
    swapPairs(List(1, 2, 3, 4)) shouldBe List(2, 1, 4, 3)
    swapPairs(List(1)) shouldBe List(1)
    swapPairs(List()) shouldBe List()
  }

}
