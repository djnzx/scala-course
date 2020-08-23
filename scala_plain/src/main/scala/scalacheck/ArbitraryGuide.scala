package scalacheck

import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen._
import org.scalacheck.Prop.forAll

object ArbitraryGuide extends App {
  
  val i1: Gen[Int] = arbitrary[Int]
  val pn: Option[Int] = Gen.posNum[Int].sample
  
  val complex: Gen[(String, List[String])] = for {
    a <- alphaNumStr
    b <- listOf(alphaUpperStr) 
  } yield (a, b)
  
  /** own type */
  case class Record(s: String)
  val genRecord: Gen[Record] = alphaStr.map(Record)
  implicit val arbRecord: Arbitrary[Record] = Arbitrary(genRecord)
  
  /** Arbitrary being used here  */
  forAll { r: Record =>
    true
  } check
  
  val rec1: Option[Record] = arbitrary[Record].sample
  
  
}
