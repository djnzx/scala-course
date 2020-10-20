package geeksforgeeks.p1basic

import tools.fmt.Fmt._

import scala.collection.mutable.ListBuffer

/**
  * https://www.geeksforgeeks.org/perfect-sum-problem-print-subsets-given-sum/
  * 
  * actually this is [[B011SubsetSum]]
  * https://www.geeksforgeeks.org/subset-sum-problem-dp-25/
  * but with some extra things
  */
object B014PerfectSumCollectMutable {
  
  def perfectSum(s0: Set[Int], sum: Int): Set[Set[Int]] = {
    println(s"sum: $sum")
    val sa = s0.toArray.sorted//.reverse
    val dp = Array.ofDim[Boolean](sum + 1, sa.length + 1) // by default all are false
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
    
    val result = ListBuffer.empty[Set[Int]]
    
    def collect(i: Int, sum: Int, subset: Set[Int]): Unit = {
      // termination with empty result
      if (i == -1 && sum != 0) return
      
      // termination with non-empty result 
      if (sum == 0) {
        result.addOne(subset)
        return
      }

      // recursion w.o. current element
      if (dp(sum)(i)) 
        collect(i - 1, sum, subset)

      // recursion with current element
      val item = sa(i)
      if (sum >= item && dp(sum - item)(i)) 
        collect(i - 1, sum - item, subset + item)
    }
    
    if (!dp(sum)(sa.length)) 
      Set.empty
    else {
      collect(sa.length-1, sum, Set.empty)
      result.toSet
    }
  }
}
