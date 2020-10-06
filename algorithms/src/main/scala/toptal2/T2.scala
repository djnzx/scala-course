package toptal2

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object T2 {
  /**
    * minimal length
    * of subarray, containing
    * all unique elements
    * 
    * * [7, 3, 7, 3, 1, 3, 4, 1] -> 5: [7, 3, 1, 3, 4]
    * * [2, 1, 1, 3, 2, 1, 1, 3] -> 3: [3, 2, 1]
    * * [7, 5, 2, 7, 2, 7, 4, 7] -> 6: [5, 2, 7, 2, 7, 4]
    */
  def solution(a: Array[Int]): Int = {
    require(a.length > 0, "array expected to have at least one element")
    val c = new Counter[Int]
    val N = a.length
    val K = a.distinct.length
    // initial
    var i = 0
    var j = -1
    while (c.size() < K) {
      j += 1
      c inc a(j)
    }
    // always actual positions, no +-1
    var l = 0
    var r = j

    def pullTail = while (c.get(a(i)) > 1) {
      c dec a(i)
      i += 1
    }
    
    def relax = if (j - i < r - l) {
      l = i
      r = j
    } 
    
    def moveBy1 = {
      j += 1
      c inc a(j)
    }

    pullTail; relax
    while (r-l+1 > K && j < N-1) {
      moveBy1
      pullTail; relax
    }
    
    r-l+1
  }

}
