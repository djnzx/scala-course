package hackerrankfp.d200426.subsetsum

import java.util

import scala.collection.Searching

object SubSumApp extends App {
  val data = Seq(8, 7, 6, 5, 4, 3, 2, 1)

  def subsums(xs: Seq[Int]) = xs
    .foldLeft(List(0L)) {
      case (rh :: rt, x) => (rh + x) :: rh :: rt
      case _             => ???
    }
    .reverse
    .toArray

  def findSize(xs: Array[Long], value: Long): Int = xs.search(value) match {
    case Searching.Found(index)       => index
    case Searching.InsertionPoint(ip) => if (ip >= xs.length) -1 else ip
  }

  val subs = subsums(data)
  println(util.Arrays.toString(subs))
  println(findSize(subs, 10)) // 2
  println(findSize(subs, 8)) // 1
  println(findSize(subs, 36))
  println(findSize(subs, 47))

}
