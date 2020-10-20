package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/perfect-sum-problem-print-subsets-given-sum/
  * 
  * actually this is [[B011SubsetSum]]
  * https://www.geeksforgeeks.org/subset-sum-problem-dp-25/
  * but with some extra things
  */
class B014PerfectSumSpec extends ASpec {
  it("1") {
    val data = Seq(
      (Set(2,3,5,6,8,10),10) -> Set(
        Set(2,3,5),
        Set(2,8),
        Set(10),
      ),
      (Set(1,2,3,4,5),10) -> Set(
        Set(4,3,2,1),
        Set(5,3,2),
        Set(5,4,1),
      ),
    )
    val impls = Seq(
      (B014PerfectSumCollectMutable.perfectSum _).tupled,
      (B014PerfectSumCollectImmutable.perfectSum _).tupled,
    )
    
    runAllSD(data, impls)
  }
  
}
