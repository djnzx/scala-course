package kand

import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite

class Lit extends AnyFunSuite {

  val l = Seq(
    "7 8 10 [11,12,18] [13,14,17] 15 16 19",
    "[105, 63, 72, 61, 9, 107, 3,59] [31, 86, 89, 95] [91] [91, 5, 92, 110] [86, 79, 80, 107, 3, 59] [62] [106, 79, 80] [8] [76] [34] [91, 30, 110, 95, 31] [34] [30] [56] [68, 66, 90, 114] [13] [8] [16] [2] [1] [11] [12] [8],11 11 4 6 7 6 6 6 7 10 17 18 19 15 16 16 12 18     ",
    "34 16 16 110 21 73 34 109 16 110 16 110 110 66 83 21 58 85 64 67 16 88 64 34 18 81 69 70 71 87 114 2 7",
    "14 7 16 17 6 15 2 4 6 2 2 2 20 14 18 19 11 "
  )

  def split(s: String) = s
    .split(' ')
    .flatMap(_.split(','))
    .flatMap(_.split('['))
    .flatMap(_.split(']'))
    .filter(_.nonEmpty)
    .map(_.toInt)

  def distinct(xs: Seq[Int]) =
    xs.toSet
      .toVector
      .sorted

  test("number of used") {
    val n = l.flatMap(split).toSet.toVector.size
    println(n) // 59
  }

  def count() = l.flatMap(split)
    .groupBy(identity)
    .fmap(_.size)

  test("no ref") {
    val cnt = count()
    (1 to 115)
      .map(n => n -> cnt.get(n))
      .map {
        case (n, Some(c)) => s"$n\t$c"
        case (n, _) => n.toString
      }
      .foreach(println)
  }

  test("number with quantity") {
    val xs1 = count()
      .toList
      .sortBy { case (k, _) => k }

//    pprint.log(xs1)
    xs1.foreach{ case (n, c) => println(s"$n\t$c")}

  }

}
