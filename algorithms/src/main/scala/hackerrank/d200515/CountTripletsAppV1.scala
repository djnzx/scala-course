package hackerrank.d200515

/**
  * https://www.hackerrank.com/challenges/count-triplets-1/problem?h_l=interview&playlist_slugs%5B%5D=interview-preparation-kit&playlist_slugs%5B%5D=dictionaries-hashmaps
  * 8 of 13 Your code did not execute within the time limits
  */
object CountTripletsAppV1 extends App {

  case class IV(i: Int, v: Long)
  
  def countTriplets(a: Array[Long], r: Long): Long = {
    val ai = a.zipWithIndex.map { case (v, i) => IV(i,v) }.toVector
    var cnt = 0L
    (0 to a.length-3).foreach { i =>
      val x1 = a(i)*r
      val x2 = x1*r

      val x1x2: Vector[IV] = ai  .filter { case IV(idx, v) => v == x1 || v == x2 && idx > i }
      val ax1:  Vector[IV] = x1x2.filter { case IV(idx, v) => v == x1            && idx > i }

      cnt +=
        ax1.flatMap { iv1 =>
          x1x2
            .filter { case IV(idx, v) => v == x2 && idx > iv1.i }
        }
          .length

    }
    cnt
  }
}
