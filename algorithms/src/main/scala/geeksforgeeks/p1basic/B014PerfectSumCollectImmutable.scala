package geeksforgeeks.p1basic

import tools.fmt.Fmt._

/**
  * https://www.geeksforgeeks.org/perfect-sum-problem-print-subsets-given-sum/
  *
  * actually this is [[B011SubsetSum]]
  * https://www.geeksforgeeks.org/subset-sum-problem-dp-25/
  * but with some extra things
  */
object B014PerfectSumCollectImmutable {

  def perfectSum(s0: Set[Int], sum: Int): Set[Set[Int]] = {
    println(s"sum: $sum")
    val sa = s0.toArray.sorted//.reverse
    val dp = Array.ofDim[Boolean](sum + 1, sa.length + 1) // by default all are false

    def solve() = {
      (0 to sa.length).foreach(dp(0)(_) = true)             // base case 1: if sum = 0 the answer is true

      (1 to sum).foreach { psum =>                          // outer iteration by partial sum 1...sum
        sa.indices.foreach { j =>                           // inner iteration by set items
          val elem = sa(j)
          dp(psum)(j+1) = dp(psum)(j) || (psum >= elem && dp(psum - elem)(j))
        }
      }

      printIndices(sa.indices, 3, "index:     ")
      printArray(sa, 3, "value:   ")
      printLine(30, '-')

      printIndices(dp(0).indices, 3, "dp indx:")
      printLine(30)
      var v = -1
      printBoolMatrix(dp, linePrefix = () => { v+=1; s"ss=${rightPad(v, 2)} " }, width = 3)
    }

    solve()

    def collect(i: Int, sum: Int, part: Set[Int]): Set[Set[Int]] = {
      // successful termination with non-empty result
      if (sum == 0) return Set(part)
      // termination with empty result
      if (i == -1) return Set()
      // current element
      val curr = sa(i)
      // if current is bigger than sub sum -> skip it, we can't use it
      if (curr > sum) return collect(i - 1, sum, part)
      // recursion branch w.o. current element
      val withoutCurr = if (dp(sum)       (i)) collect(i - 1, sum,        part       ) else Set()
      // recursion branch with current element
      val withCurr    = if (dp(sum - curr)(i)) collect(i - 1, sum - curr, part + curr) else Set()

      withoutCurr ++ withCurr
    }

    if (dp(sum)(sa.length))
      collect(sa.length-1, sum, Set.empty)
    else Set()
  }
}
