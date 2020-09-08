package google

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task1Spec extends AnyFunSpec with Matchers {
  import Task1._

  describe("mkIntervals") {

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
  
  describe("process") {
    it("a") {
      val DATA = Seq(
        Input(Interval(1,7), "Jim"),
        Input(Interval(5,6), "Jeremy"),
        Input(Interval(5,10), "Alice"),
        Input(Interval(7,20), "Xen"),
        Input(Interval(30,40), "Serge"),
      )
      val EXPECTED = Seq(
        Output(Interval(1, 5),  Seq("Jim")),
        Output(Interval(5, 6),  Seq("Jim", "Jeremy", "Alice")),
        Output(Interval(6, 7),  Seq("Jim", "Alice")),
        Output(Interval(7, 10), Seq("Alice", "Xen")),
        Output(Interval(10,20), Seq("Xen")),
        Output(Interval(30,40), Seq("Serge")),
      )
      
      process(DATA) should contain theSameElementsInOrderAs EXPECTED
      process(DATA).map(_.int.min) shouldBe sorted
    }
  }
  
}
