package mortals

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import pprint.{pprintln => println}

class MathSetOperationSpec extends AnyFunSpec with Matchers {
  describe("set math operations") {
    val set1: Set[Int] = Set(1,2,3,4)
    val set2: Set[Int] = Set(3,4,5,6)
    val UNION = Set(1,2,3,4,5,6)
    val INTERSECTION = Set(3,4)
    val UNIQUE1 = Set(1,2)
    val UNIQUE2 = Set(5,6)
    val UNIQUE = Set(1,2,5,6)
    
    it("union") {
      set1 union set2 shouldEqual UNION
    }
    it("intersect") {
      set1 intersect set2 shouldEqual INTERSECTION
    }
    it("unique from 1") {
      (set1 -- set2) shouldEqual UNIQUE1
    }
    it("unique from 2") {
      (set2 -- set1) shouldEqual UNIQUE2
    }
    it("unique from both") {
      (set1 union set2) -- (set1 intersect set2) shouldEqual UNIQUE
    }
    
  }
}
