package fp_red.red04

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class OptionSpec extends AnyFunSpec with Matchers {
  describe("Option: Traverse and Sequence") {

    import OptionTrSeq._
    
    describe("traverse") {
      val dataS: List[Int] = (1 to 5).toList
      val dataX1: List[Int] = (1 to 5).map(_+10).toList
      def f1(x: Int): Option[Int] = Some(x + 10) 
      def f2(x: Int): Option[Int] = if (x % 2 == 0) Some(x + 10) else None 
        
      it("1") {
        traverse(dataS)(f1) shouldBe Some(dataX1)
      }
      it("1a") {
        traverse_via_sequence(dataS)(f1) shouldBe Some(dataX1)
      }
      it("2") {
        traverse(dataS)(f2) shouldBe None
      }
    }
    describe("sequence") {

      val dataX: List[Int] = (1 to 5).toList
      val dataS: List[Option[Int]] = dataX.map(Some(_)).toList
      
      val dataN1: List[Option[Int]] = List(None)
      val dataN2: List[Option[Int]] = None :: dataS
      val dataN3: List[Option[Int]] = dataS :+ None
      val dataN4: List[Option[Int]] = dataS ::: None +: dataS
      
      it("1") {
        sequence(dataS) shouldBe Some(dataX)
      }
      it("1a") {
        sequence_via_traverse(dataS) shouldBe Some(dataX)
      }
      it("2") {
        sequence(dataN1) shouldBe None
      }
      it("3") {
        sequence(dataN2) shouldBe None
      }
      it("4") {
        sequence(dataN3) shouldBe None
      }
      it("5") {
        sequence(dataN4) shouldBe None
      }
    }
  }
}
