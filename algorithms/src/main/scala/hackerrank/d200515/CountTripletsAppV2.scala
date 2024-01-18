package hackerrank.d200515

/**
  * https://www.hackerrank.com/challenges/count-triplets-1/problem?h_l=interview&playlist_slugs%5B%5D=interview-preparation-kit&playlist_slugs%5B%5D=dictionaries-hashmaps
  * 8 of 13 Your code did not execute within the time limits
  */
object CountTripletsAppV2 extends App {

  def countOnly(a: Array[Long], x: Long, from: Int) =
    (from until a.length).count { a(_) == x }

  def findIndexes(a: Array[Long], x: Long, from: Int) =
    (from until a.length-1)
      .collect { case idx if a(idx) == x => idx }
      .toArray

  def countTriplets(a: Array[Long], r: Long): Long = {
    var count = 0L
    (0 until a.length-2).foreach { i1 =>
      val x2 = a(i1) * r
      val seconds = findIndexes(a, x2, i1+1)
      seconds.foreach { i2 =>
        count += countOnly(a, x2 * r, i2 + 1)
      }
    }
    count
  }
  
}
