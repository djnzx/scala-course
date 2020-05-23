package hackerrank.d200421_07

import java.util

/**
  * Counting Sort 2
  * https://www.hackerrank.com/challenges/countingsort2/problem
  */
object CountingSort2 extends App {

  def countingSort(arr: Array[Int]): Array[Int] = {
    val c = Array.fill[Int](100)(0)
    arr.foreach { item => c(item) = c(item) + 1 }
    c.zip(0 to 99)
      .filter(t => t._1 > 0)
      .flatMap(t => Array.fill[Int](t._1)(t._2))
  }

  val si = "63 25 73 1 98 73 56 84 86 57 16 83 8 25 81 56 9 53 98 67 99 12 83 89 80 91 39 86 76 85 74 39 25 90 59 10 94 32 44 3 89 30 27 79 46 96 27 32 18 21 92 69 81 40 40 34 68 78 24 87 42 69 23 41 78 22 6 90 99 89 50 30 20 1 43 3 70 95 33 46 44 9 69 48 33 60 65 16 82 67 61 32 21 79 75 75 13 87 70 33"
    .split(" ")
    .map { _.toInt }

  val r = countingSort(si)
  println(s"List${util.Arrays.toString(r)}")
}
