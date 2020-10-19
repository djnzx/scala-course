package geeksforgeeks.p1basic

import tools.fmt.Fmt.printBoolMatrix

/**
  * https://www.geeksforgeeks.org/subset-sum-problem-dp-25/
  * Given a set of non-negative integers, and a value sum,
  * determine if there is a subset of the given set with sum equal to given sum
  *
  * NP-Complete:
  * https://en.wikipedia.org/wiki/NP-completeness
  *
  */
/**
  * plain recursion (math)
  * exponential
  */
object B011SubsetSum {

  def subsetSum(s: Set[Int], sum: Int): Boolean = {
    if (sum == 0) return true
    if (sum < 0) return false
    if (s.isEmpty) return false

    val x = s.head

    subsetSum(s - x, sum) ||
    subsetSum(s - x, sum - x)
  }
}

object B011SubsetSumDP {

  def subsetSum(s0: Set[Int], sum: Int): Boolean = {
    val sa = s0.toArray
    val dp = Array.ofDim[Boolean](sum + 1, sa.length + 1) // by default all are false
    (0 to sa.length).foreach(dp(0)(_) = true)             // if sum = 0 the answer is true

    (1 to sum).foreach { psum =>                          // outer iteration by partial sum 1...sum
      sa.indices.foreach { j =>                           // inner iteration by set items
        val elem = sa(j)
//        dp(i)(j+1) =
//          if (i >= sa(j))  dp(i)(j) || dp(i - sa(j))(j)
//          else             dp(i)(j)
        dp(psum)(j+1) = dp(psum)(j) || (psum >= elem && dp(psum - elem)(j))
      }
    }

//    printBoolMatrix(dp)
    dp(sum)(sa.length)
  }
}
