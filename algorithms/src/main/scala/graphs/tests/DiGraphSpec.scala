package graphs.tests

import graphs.impls.LongestPathImpl
import graphs.rep.DiGraphA
import tools.spec.ASpec

class DiGraphSpec extends ASpec {
  
  val testGraph = Seq(
    (0,1),
    (0,2),
    (1,2),
    
    (3,4),
    (4,5),
    (3,5),
    
    (6,7),
    (6,8),
    (6,9),
    (7,8),
    (7,9),
    (8,9),
    (1,3),
  )
  val graph = DiGraphA.from(10, testGraph)
  
  it("1") {
    println(graph)
  }
  
  it("2") {
    val lp = new LongestPathImpl(graph)
    println(graph)
    println(lp.longestLength)
  }
  
}
