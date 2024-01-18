package hackerrank.d200421

/**
  * Counting Sort 1
  * https://www.hackerrank.com/challenges/countingsort1/problem
  */
object CountingSort1 extends App {

  def countingSort(arr: Array[Int]): Array[Int] = {
    val a: Array[Int] = Array.fill[Int](100)(0)
    arr.foreach { item => a(item) = a(item) + 1 }
    a
  }

}
