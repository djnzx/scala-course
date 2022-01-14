package scalacheck

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.posNum
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.forAllNoShrink
import org.scalacheck.Prop.passed
import org.scalacheck.Prop.propBoolean

object Guide6Tips extends App {

  /** 1. prefer generators over ==> */

  forAll { (i1: Int, i2: Int, i3: Int) =>
    (i1 > 0 && i2 > 0 && i3 > 0) ==> {
      passed
    }
  }

  /** ok */
  forAll(posNum[Int], posNum[Int], posNum[Int]) { (i1, i2, i3) =>
    passed
  }

  forAll(arbitrary[Int], arbitrary[Map[Int, String]]) { (i, m) =>
    m.contains(i)
  }

  /** mark output */
  forAll("Index" |: arbitrary[Int], "Lookup DB" |: arbitrary[Map[Int, String]]) { (i, m) =>
    m.contains(i)
  }

  forAllNoShrink { (i: Int, j: Int) =>
    val (max, min) = (i max j, i min j)
    val (maxSq, minSq) = (max * max, min * min)
    s"[min: $min, minSq: $minSq], [max: $max, maxSq: $maxSq]" |: (minSq < maxSq)
  }

}
