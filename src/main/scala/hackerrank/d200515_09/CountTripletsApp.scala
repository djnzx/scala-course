package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/count-triplets-1/problem?h_l=interview&playlist_slugs%5B%5D=interview-preparation-kit&playlist_slugs%5B%5D=dictionaries-hashmaps
  * doesn't work
  */
object CountTripletsApp extends App {

  def countTriplets(a: Array[Long], r: Long): Long = {
    val len = a.length

    (0 to len-3).flatMap { i =>
      (i to len-2).flatMap { j =>
        (j to len-1).map { k =>
          (i,j,k)
        }
      }
    }
      .count { case (i,j,k) => val x = a(i)*r; a(j) == x && a(k) == x*r }
  }
  // exp: 161700
  // fact: 171598
  println(countTriplets(Array.fill[Long](100)(1),1))
}
