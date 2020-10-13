package geeksforgeeks.p1basic

/**
  * https://www.geeksforgeeks.org/subset-sum-problem-osum-space/
  * 
  * since we don't need more than one step back,
  * we can reduce the amount of space from O(sum*set.size) to O(sum)
  */
object B012SubsetSumDPSpaceOptimized {

  def subsetSum(s0: Set[Int], sum: Int): Boolean = {
    val sa = s0.toArray
    val N = sa.length
    val dp = Array.ofDim[Boolean](2, sum + 1)
    
    (0 to N).foreach { ii =>
      (0 to sum).foreach { j =>
        val i    = ii & 1
        val prev = 1 - i
        
        dp(i)(j) =
          if      (j == 0)  true
          else if (ii == 0) false
          else              dp(prev)(j) || sa(ii - 1) <= j && dp(prev)(j - sa(ii - 1))
      }
    }
    dp(N % 2)(sum)
  }
}

object B012SubsetSumDPFP {

  def subsetSum(s: Set[Int], sum: Int): Boolean = {
    val initial = Array.ofDim[Boolean](sum + 1)
    initial(0) = true

    s.foldLeft(initial) { (prev, num) =>
      val next = initial.clone()
      
      (1 to sum).foreach { sm =>
        next(sm) = prev(sm) || num <= sm && prev(sm - num)
      }
      
      next
    }
      .last
  }
}
