package geeksforgeeks.p1basic

import tools.fmt.Fmt.{printArray, printBoolMatrix, rightPad, printIndices, printLine}
import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/perfect-sum-problem-print-subsets-given-sum/
  * 
  * actually this is [[B011SubsetSum]]
  * https://www.geeksforgeeks.org/subset-sum-problem-dp-25/
  * but with some extra things
  */
object B014PerfectSum {
  
  def perfectSum(s0: Set[Int], sum: Int): Seq[Set[Int]] = {
    println(s"sum: $sum")
    val sa = s0.toArray.sorted//.reverse
    val dp = Array.ofDim[Boolean](sum + 1, sa.length + 1) // by default all are false
    (0 to sa.length).foreach(dp(0)(_) = true)             // if sum = 0 the answer is true

    (1 to sum).foreach { psum =>                          // outer iteration by partial sum 1...sum
      sa.indices.foreach { j =>                           // inner iteration by set items
        val elem = sa(j)
        dp(psum)(j+1) = dp(psum)(j) || (psum >= elem && dp(psum - elem)(j))
      }
    }
    
    printIndices(sa.indices, 3, "index:     ")
    printArray(sa, 3, "value:   ")
    printLine(30, '-')

    printIndices(dp(0).indices, 3, "mat.ind:")
    printLine(30)
    var v = -1
    printBoolMatrix(dp, linePrefix = () => { v+=1; s"ss=${rightPad(v, 2)} " }, width = 3)
    dp(sum)(sa.length)
    // TODO: calculate the path (build all possible combinations)
    Seq(Set())
  }
}

class B014PerfectSumSpec extends ASpec {
  import B014PerfectSum._
  
  it("1") {
    val data = Seq(
      (Set(2,3,5,6,8,10),10) -> Seq(
        Set(2,3,5),
        Set(2,8),
        Set(10),
      ),
      (Set(1,2,3,4,5),10) -> Seq(
        Set(4,3,2,1),
        Set(5,3,2),
        Set(5,4,1),
      )
    )
    
    runAllD(data, (perfectSum _).tupled)
  }
  
}
