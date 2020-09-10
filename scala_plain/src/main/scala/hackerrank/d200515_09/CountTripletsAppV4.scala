package hackerrank.d200515_09

import scala.collection.Searching

/**
  * Idea:
  * - sort data,
  * - represent it with pairs (index, value),
  * - apply binary search on sorted data to avoid N^3^ complexity
  * iterate through array and look for left and right
  */
object CountTripletsAppV4 extends App {

  /** LogN */
  def toL(poss: Array[Int], curr_idx: Int) =
    (poss.search(curr_idx) match {
      case Searching.Found(idx)                             => idx + 1 
      case Searching.InsertionPoint(ip) if ip < poss.length => ip
      case _                                                => poss.length
    }).toLong

  /** LogN */
  def toR(poss: Array[Int], curr_idx: Int) =
    (poss.search(curr_idx) match {
      case Searching.Found(idx)                             => poss.length - idx 
      case Searching.InsertionPoint(ip) if ip < poss.length => poss.length - ip
      case _                                                => 0
    }).toLong

  def countTriplets(a: Array[Long], r: Long): Long = {
    /** N*logN */
    val cache = a.zipWithIndex.groupMap(_._1)(_._2).map { case (k, v) => (k, v.sorted) }
    
    /** N */
    (1 until a.length-1)
      .withFilter(i => a(i) % r == 0 && cache.contains(a(i)/r) && cache.contains(a(i)*r))
      .map(i => toL(cache(a(i)/r), i-1) * toR(cache(a(i)*r), i+1))
      .sum
  }
  
}
