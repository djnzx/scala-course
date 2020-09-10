package hackerrank.d200515_09

import scala.collection.Searching
import pprint.{pprintln => println}

/**
  * Idea:
  * - sort data,
  * - represent it with pairs (index, value),
  * - apply binary search on sorted data to avoid N^3^ complexity
  */
object CountTripletsAppV3 extends App {

  def countFrom3(poss: Array[Int], from: Int) =
    poss.search[Int](from) match {
      case Searching.Found(idx) => poss.length - idx 
      case Searching.InsertionPoint(ip) if ip < poss.length => poss.length - ip
      case _ => 0
    }

  // IK that item exists, but IDK in which poss   
  def findFirstFrom2(poss: Array[Int], from: Int) = 
    poss.search[Int](from) match {
      case Searching.Found(idx) => Some(idx) 
      case Searching.InsertionPoint(ip) if ip < poss.length => Some(ip)
      case _ => None
    }

  def countTriplets(a: Array[Long], r: Long): Long = {
    var count = 0L
    val map: Map[Long, Array[Int]] = a.zipWithIndex.groupMap(_._1)(_._2)
      .map { case (k, v) => (k, v.sorted) }
    (0 until a.length-2).foreach { i1 =>
      val x1 = a(i1)
      val x2 = x1 * r
      val x3 = x2 * r
      if (map.contains(x2) && map.contains(x3)) {
        val x2indices = map(x2) // all x2 indices
        val x3indices = map(x3) // all x3 indices
        /** first index for the second pass */
        val x2io: Option[Int] = findFirstFrom2(x2indices, i1+1)
        x2io.foreach { x2first: Int =>
          (x2first until x2indices.length).foreach { x2i => 
            count += countFrom3(x3indices, x2i + 1)
          }
        }
      }
    }
    count
  }
  
//  println(countTriplets(Array(1,2,2,4), 2))
  println(countTriplets(Array(1, 3, 9, 9, 27, 81), 3))
}
