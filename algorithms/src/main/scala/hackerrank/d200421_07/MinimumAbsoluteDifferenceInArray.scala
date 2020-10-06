package hackerrank.d200421_07

/**
  * https://www.hackerrank.com/challenges/minimum-absolute-difference-in-an-array/problem
  */
object MinimumAbsoluteDifferenceInArray extends App {

  /**
    * O(N*N / 2)
    */
  def minimumAbsoluteDifference0(arr: Array[Int]): Int =
    arr.indices.flatMap { i1 =>
      arr.indices
        .filter { _ > i1 }
        .map { i2 => scala.math.abs(arr(i1) - arr(i2)) }
    }
      .min

  /**
    * O(N * logN) + 2(N-1)
    */
  def minimumAbsoluteDifference1(arr: Array[Int]): Int = {
    val sorted = arr.sorted
    (0 until sorted.length-1)
      .map { i => scala.math.abs(sorted(i) - sorted(i+1)) }
      .min
  }

  /**
    * O(N * logN) + N-1
    */
  def minimumAbsoluteDifference(arr: Array[Int]): Int = {
    val sorted = arr.sorted
    (0 until sorted.length-1)
        .foldLeft(Integer.MAX_VALUE) { (min, idx) =>
          val diff = scala.math.abs(sorted(idx) - sorted(idx+1))
          if (diff < min) diff else min
        }
  }

  println(minimumAbsoluteDifference0(Array(3, -7, 0)))
  println(minimumAbsoluteDifference(Array(3, -7, 0)))
  println(Integer.MAX_VALUE)
}
