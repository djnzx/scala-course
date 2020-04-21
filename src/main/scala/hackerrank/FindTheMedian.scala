package hackerrank

/**
  * https://www.hackerrank.com/challenges/find-the-median/problem
  */
object FindTheMedian extends App {

  def findMedian(arr: Array[Int]): Int = {
    val c = Array.fill[Int](20001)(0)
    arr.foreach { item =>
      val idx = item + 10000
      c(idx) = c(idx) + 1 }
    val sorted = c.zip(-10000 to 10000)
      .filter(t => t._1 > 0)
      .flatMap(t => Array.fill[Int](t._1)(t._2))
    sorted(sorted.length/2)
  }

  val si = "0 1 2 4 6 5 3"
    .split(" ")
    .map { _.toInt }

  println(findMedian(si))


}
