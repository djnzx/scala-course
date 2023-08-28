package scalacheck

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop._

/** experiment, fail, learn! fixing test => fixing your mental model */
object Guide1A extends App {

  /** we define the property, string provided randomly */
  val p1: Prop = forAll { s: String =>
    s.length >= 0
  }

  /** we check it */
  p1.check

}

/** it will fail for x = -2147483648 */
object Guide2Fail extends App {

  val p2: Prop = forAll { x: Int =>
    Math.abs(x) >= 0
  }

  p2.check()

}

/** will pass because we added extra filter */
object Guide2Pass extends App {

  forAll { x: Int =>
    x > Integer.MIN_VALUE ==> // combination operator
      Math.abs(x) >= 0
  } check

}

/** shrinking ! */
object Guide3Fail extends App {

  def brokenReverse[A](xs: List[A]): List[A] =
    if (xs.length > 4) xs else xs.reverse

  /** wrong description, list can be Nil */
  forAll { xs: List[Int] =>
    xs.last == brokenReverse(xs).head
  } check

}

object Guide4Fail extends App {

  /** it will fail, when l2 is empty */
  forAll { (l1: List[Int], l2: List[Int]) =>
    l1.length < (l1 ::: l2).length
  } check

}

object Guide4Fixed extends App {

  forAll { (l1: List[Int], l2: List[Int]) =>
    l1.length <= (l1 ::: l2).length
  } check

}

/** Generators. we can specify exactly with which kind of data we work */
object Guide4Pass extends App {

  /** non empty list of random ints */
  val genList: Gen[List[Int]] = Gen.nonEmptyListOf(arbitrary[Int])

  /** since they are non-empty, it will work */
  forAll(genList, genList) { (l1, l2) =>
    l1.length < (l1 ::: l2).length
  } check

}
