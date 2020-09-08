package google

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task1Spec extends AnyFunSpec with Matchers {

  describe("mkIntervals") {
    import Task1._

    it("empty") {
      val DATA     = Seq()
      val EXPECTED = Seq()
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }
    
    it("one element") {
      val DATA     = Seq(Interval(1, 100))
      val EXPECTED = Seq(Interval(1, 100))
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }
    
    it("non-empty sorted") {
      val DATA     = Seq(Interval(1, 100), Interval(10, 70), Interval(20, 90))
      val EXPECTED = Seq(Interval(1, 10), Interval(10, 20), Interval(20, 70), Interval(70, 90), Interval(90, 100))
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }

    it("non-empty unsorted") {
      val DATA     = Seq(Interval(1, 100), Interval(20, 90), Interval(10, 70))
      val EXPECTED = Seq(Interval(1, 10), Interval(10, 20), Interval(20, 70), Interval(70, 90), Interval(90, 100))
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }

    it("non-empty unsorted nested") {
      val DATA     = Seq(Interval(1, 100), Interval(20, 70), Interval(10, 90))
      val EXPECTED = Seq(Interval(1, 10), Interval(10, 20), Interval(20, 70), Interval(70, 90), Interval(90, 100))
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }

    it("non crossing") {
      val DATA     = Seq(Interval(20, 30), Interval(1, 10), Interval(100, 900))
      val EXPECTED = Seq(Interval(1, 10), Interval(10, 20), Interval(20, 30), Interval(30, 100), Interval(100, 900))
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }

    it("non sotred") {
      val DATA     = Seq(Interval(100, 900), Interval(20, 30), Interval(1, 10))
      val EXPECTED = Seq(Interval(1, 10), Interval(10, 20), Interval(20, 30), Interval(30, 100), Interval(100, 900))
      
      mkIntervals(DATA) shouldEqual EXPECTED
    }
  }
  
}
