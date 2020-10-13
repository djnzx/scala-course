package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * B011
  * B012
  */
class B011SubsetSumSpec extends ASpec {
  
  it("1") {
    
    val t = Seq(
      (Set(1,2,3,4), 10),
      (Set(1,2,3,4,5,6,7,8), 12),
      (Set(3,4,5,2), 9),
      (Set(3,34,4,12,5,2), 9),
    ).map(_->true)
    
    val f = Seq(
      (Set(1,2,3,4), 11),
      (Set(2,4,6,8), 11),
      (Set(3,34,4,12,5,2), 30),
    ).map(_->false)
    
    val impls = Seq(
      (B011SubsetSumDP.subsetSum _).tupled,
      (B012SubsetSumDPSpaceOptimized.subsetSum _).tupled,
      (B012SubsetSumDPFP.subsetSum _).tupled,
    )
    
    runAllSD(t ++ f, impls)
  }
  
}