package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/subset-sum-divisible-m/
  * https://www.geeksforgeeks.org/discrete-mathematics-the-pigeonhole-principle/
  */
object B013SubsetWithSumDivisibleByM {
  
  def isDivisible(a: Array[Int], sum: Int) = {
    /**
      * if array length is bigger than sum - we have result immediately
      */
    if (a.length > sum) true
    else {
      // This array will keep track of all 
      // the possible sum (after modulo m) 
      // which can be made using subsets of a[] 
      val dp = Array.ofDim[Boolean](sum)
      
      a.foreach { ai =>
        // To store all the new encountered sum (after modulo). 
        // It is used to make sure that a[i] is added only to  
        // those entries for which DP[j]  was true before current iteration.
        val t = Array.ofDim[Boolean](sum)
        // For each element of a[], we iterate over all elements of DP table  
        // from 1 to m and we add current element i. e., a[i] to all those  
        // elements which are true in DP table 
        (0 until sum).foreach { sub_sum =>
          if (dp(sub_sum) && (!dp((sub_sum + ai) % sum))) t((sub_sum + ai) % sum) = true
        }
        // Updating all the elements of temp to DP table 
        // since iteration over j is over 
        (0 until sum).foreach { sub_sum => 
          if (t(sub_sum)) dp(sub_sum) = true
        }
        // Also since a[i] is a single element subset,
        // arr[i]%m is one of the possible sum 
        dp(ai % sum) = true
      }
      
      dp(0)
    }
  }
}

class B013SubsetWithSumDivisibleByMSpec extends ASpec {
  import B013SubsetWithSumDivisibleByM._
  it("1") {
    val t = Seq(
      (Array(1,2,3,4,5,6), 3),
      (Array(115,235,659,451,326), 4),
    ).map(_->true)
    val f = Seq(
      (Array(1,2,3), 7),
    ).map(_->false)
    
    runAllD(t ++ f, (isDivisible _).tupled )
  }
  
}