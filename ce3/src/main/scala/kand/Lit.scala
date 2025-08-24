package kand

import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite

class Lit extends AnyFunSuite {

  val l = Seq(
    /* 0 */ "[7] [8] [10] [11,12,18] [13,14,17] [15] [16] [19] ",
    /* 1 */ "[105, 63, 72, 61, 9, 107, 3, 59] [31, 86, 89, 95] [91] [5, 91, 92, 110] [86, 79, 80, 107, 3, 59] [62] [106, 79, 80] [8] [76] [76] [34] [91, 30, 110, 95, 31] [34] [34] [30] [56][68, 66, 90, 114][13][8][16][2][1][11][11][11][11][12][8][11][11][4, 6][7][6][6][6, 7][10, 17][18, 19][15][16][12][18]",
    /* 2 */ "[34] [16] [16] [110] [21, 73][34][109][16][110][71][16][110][110][66][83][21][58][85][64][67][16][88][64][34][17][81][69][70][71][87][114][2,7]",
    /* 3 */ "",
    /* 4 */ "[14][7][16, 17][6][15][2, 4, 6][2][2][20, 14][18, 19, 11]",
  )

  val cleaned: Seq[Int] = l
    .flatMap(_.split(' '))
    .flatMap(_.split(','))
    .flatMap(_.split('['))
    .flatMap(_.split(']'))
    .filter(_.nonEmpty)
    .map(_.toInt)

  val max: Int = cleaned.max

  val listAll = 1 to max

  val distinct: Vector[Int] = cleaned
    .toSet
    .toVector
    .sorted

  test("1") {
    distinct.foreach(println)
  }

  val used_count = distinct.size // 58 of 114

  test("2") {
    pprint.log(used_count)
  }

  val withCount: Map[Int, Int] =
    cleaned.groupMapReduce(identity)(_ => 1)(_ + _)

  val withCountSorted: Vector[(Int, Int)] =
    withCount.toVector
      .sortBy(_._1) // id

  test("3") {
    withCountSorted
      .foreach { case (id, cnt) => println(s"$id -> $cnt") }
  }

  test("4 used / unused") {
    listAll
      .map {
        case id if withCount.contains(id) => s"$id -> ${withCount(id)}"
        case id                           => s"$id"
      }
      .foreach(println)
  }

  test("5 no ref") {
    val xs = listAll
      .filterNot(id => withCount.contains(id))

    xs.foreach(println)
    println(s"size: ${xs.size}")
  }

  test("6 used - unused") {
    listAll
      .map {
        case id if withCount.contains(id) => "Y"
        case _                            => "N"
      }
      .foreach(println)
  }

  test("6 usage count") {
    listAll
      .map(id => id -> withCount.get(id))
      .map {
        case (id, Some(n)) => s"$id -> $n"
        case (id, None)    => s"$id -> 0"
      }
      .foreach(println)
  }

}
