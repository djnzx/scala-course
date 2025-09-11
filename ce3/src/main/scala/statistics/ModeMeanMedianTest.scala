package statistics

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ModeMeanMedianTest extends AnyFunSuite with Matchers {

  def mode[A](xs: Seq[A]): Set[A] =
    xs.groupMapReduce(identity)(_ => 1)(_ + _)
      .groupMapReduce { case (x, cnt) => cnt } { case (x, cnt) => Set(x) }((xs, ys) => xs ++ ys)
      .reduce((a, b) => if (a._1 >= b._1) a else b)
      ._2

  def mean(xs: Seq[Int]) = xs.sum.toDouble / xs.length

  def median(xs: Seq[Int]) = {
    val l = xs.length
    l % 2 match {
      case 1 => xs(l / 2).toDouble
      case _ => (xs(l / 2) + xs(l / 2 - 1)).toDouble / 2
    }
  }

  test("mode") {

    /** unimodal */
    mode(Seq(1, 2, 3, 4, 5, 6, 7, 3)) shouldBe Set(3)

    /** bimodal */
    mode(Seq(1, 2, 3, 4, 5, 6, 7, 3, 7)) shouldBe Set(3, 7)

    /** multimodal */
    mode(Seq(1, 2, 3, 4, 5, 1, 2, 3)) shouldBe Set(1, 2, 3)

  }

  test("mean / avg") {
    mean(Seq(100_000, 120_000, 150_000, 180_000, 5_000_000)) shouldBe 1_110_000
  }

  test("median - odd") {
    median(Seq(100_000, 120_000, 150_000, 180_000, 5_000_000)) shouldBe 150_000
  }

  test("median - even") {
    median(Seq(100_000, 120_000, 150_000, 160_000, 180_000, 5_000_000)) shouldBe 155_000
  }

}
