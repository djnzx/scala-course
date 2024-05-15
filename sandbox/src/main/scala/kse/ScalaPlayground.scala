package kse

import common.Base

class ScalaPlayground extends Base {

  /** (x, y) => math.min(x, y)
    * (x, y) => math.max(x, y)
    */
  def min(xs: Seq[Int]): Int =
    xs.reduce((a, b) => math.min(a, b))

  def max(xs: Seq[Int]): Int =
    xs.reduce((a, b) => math.max(a, b))

  def sum(xs: Seq[Int]): Int =
    xs.foldLeft(0)((a, x) => a + x)

  def contains(xs: Seq[Int], x0: Int): Boolean =
    xs.foldLeft(false)((found, x) => found || x0 == x)

  test("1") {
    val data = List(1, 2, 3, 4, 5)

    val l1 = List(1, 2)
    val l2 = List(3, 4)
    val l3 = l1 ++ l2

    pprint.log(l3) // List(1, 2, 3, 4)

    data.min
    data.max
    data.sum
    data.product
    data.contains(3)

    val mn = min(data)
    val mx = max(data)
    val sm = sum(data)

    pprint.log(mn)
    pprint.log(mx)
    pprint.log(sm)
    pprint.log(contains(data, 3))  // true
    pprint.log(contains(data, 33)) // false
  }

}
