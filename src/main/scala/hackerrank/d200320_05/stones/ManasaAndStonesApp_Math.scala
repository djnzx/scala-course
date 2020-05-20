package hackerrank.d200320_05.stones

import java.util

/**
  * https://www.hackerrank.com/challenges/manasa-and-stones/problem
  */
object ManasaAndStonesApp_Math extends App {

  def stones(n: Int, a: Int, b: Int) =
    (0 until n)
      .map { x => x * a + (n - 1 - x) * b }
      .distinct
      .sorted
      .toArray

  println(util.Arrays.toString(
    stones(4, 10, 100)
  ))
}
