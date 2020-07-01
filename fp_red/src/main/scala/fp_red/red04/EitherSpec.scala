package fp_red.red04

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class EitherSpec extends AnyFunSpec with Matchers {
  
  import EitherTrSeq._
  
  val inc: Int => Int = _ + 1
  val le:          Either[String, Nothing] = Left("3;(")
  val fae1: Int => Either[String, Int] = (a: Int) => Right(inc(a))
  val fae2: Int => Either[String, Int] = {
    case 3 => le
    case a => Right(a + 1)
  }
  val fae3: Int => Either[String, Int] = (a: Int) =>
    if (a != 3) Right(a + 1)
    else        le
  
  val data:  List[Int] = (1 to 5).toList // 1..5
  val data1: List[Int] = data.map(inc)   // 2..6
  
  val dataR1: List[Either[String, Int]] = data.map(fae1)
  val dataE0: List[Either[String, Int]] = List(le) 
  val dataE1: List[Either[String, Int]] = data.map(fae1) :+ le 
  val dataE2: List[Either[String, Int]] = le :: data.map(fae1) 
  
  describe("traverse R, TR, via sequence") {

    val impls: Seq[
      List[Int] => (Int => Either[String, Int]) => Either[String, List[Int]]
    ] = Seq(traverse, traverseTR, traverse_via_sequence)
    
    val par_res = Seq( 
      (data, fae1) -> Right(data1),
      (data, fae2) -> le,
      (data, fae3) -> le,
    )
    
    it("9") {
      for {
        impl            <- impls   // 3 different implementation
        ((p1, p2), res) <- par_res // 3 different data sets
      } yield impl(p1)(p2) shouldBe res
    }
  }
  
  describe("sequence R, TR, via traverse") {
    val impls: Seq[
      List[Either[String, Int]] => Either[String, List[Int]]
    ] = Seq(sequence, sequenceTR, sequence_via_traverse)
    
    val par_res: Map[List[Either[String, Int]], Either[String, List[Int]]] = Map(
      dataR1 -> Right(data1),
      dataE0 -> le,
      dataE1 -> le,
      dataE2 -> le
    )
    
    it("12") {
      for {
        impl       <- impls   // 3 different implementation
        (par, res) <- par_res // 4 different data sets
      } yield impl(par) shouldBe res
    }
  }
  
}
