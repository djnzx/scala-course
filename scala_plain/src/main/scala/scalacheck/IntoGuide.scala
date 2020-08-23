package scalacheck

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Gen, Prop}
import org.scalacheck.Prop.{forAll, propBoolean}

object IntoGuide extends App {
  
  val p1: Prop = forAll { s: String =>
    s.length >= 0
  }
  
  p1.check

  /**
    * treats as math...
    */
  forAll { x: Int =>
    Math.abs(x) >=0 
  } check
 
  forAll {x: Int =>
    x > Integer.MIN_VALUE ==>
      Math.abs(x) >=0
  } check
 
  def brokenReverse[A](xs: List[A]): List[A] =
    if (xs.length > 4) xs else xs.reverse

  /**
    * shrinking
    */
  forAll { xs: List[Int] => 
    xs.last == brokenReverse(xs).head
  } check
  
  /** beware of <=, not just < */
  forAll { (l1: List[Int], l2: List[Int]) =>
    l1.length < (l1 ::: l2).length
  } check

  forAll ( Gen.nonEmptyListOf(arbitrary[Int]), Gen.nonEmptyListOf(arbitrary[Int])) { (l1, l2) =>
    l1.length < (l1 ::: l2).length
  } check
  
  
}
