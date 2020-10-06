package codility

/**
  * Write a function:
  * def solution(a: Array[Int]): Int
  *
  * that, given an array A of N integers, 
  * returns the smallest positive integer 
  * (greater than 0) that does not occur in A.
  *
  * For example, given A = [1, 3, 6, 4, 1, 2], the function should return 5.
  * Given A = [1, 2, 3], the function should return 4.
  * Given A = [−1, −3], the function should return 1.
  *
  * N is an integer within the range [1..100,000];
  * each element of array A is an integer within the range [−1,000,000..1,000,000].
  */
object MinNonNeg2 extends App {
  
  def solution(a: Array[Int]): Int = {
    a.filter(_ > 0)
      .sorted
      .foldLeft((false, 1)) { case ((done, max), curr) => {
        if (done) (true, max)
        else {
          if (curr > max) (true, max)
          else (false, curr + 1)
        }
      }
      }
      ._2
  }

  val r = solution(Array(1, 3, 6, 4, 1, 2))
  pprint.log(r)


}
