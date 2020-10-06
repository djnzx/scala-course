package toptal2.twopointers

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import toptal2.Counter

class TwoPointersSpec extends AnyFunSpec with Matchers {

  val data = IndexedSeq(
    Array(13) -> 1,
    Array(13,13,13) -> 1,
    Array(1,2) -> 2,
    Array(7,3,7,3,1,3,4,1) -> 5,
    Array(2,1,1,3,2,1,1,3) -> 3,
    Array(7,5,2,7,2,7,4,7) -> 6,
    Array(7,5,1,1,1,1,7,5) -> 3,
    Array(3,1,1,1,1,1,2,3) -> 3,
  )

  it("iterative") {
    import TwoPointersIterative._
    
    for {
      (in, out) <- data
    } solution(in) shouldEqual out
  }  

  it("tail recursive mutable map") {
    import TwoPointersRecursive._
    
    for {
      (in, out) <- data
    } find(in, in.distinct.length) shouldEqual out
  }  

  it("tail recursive immutable map") {
    import TwoPointersRecursiveImmutable._
    
    for {
      (in, out) <- data
    } find(in, in.distinct.length) shouldEqual out
  }  
}