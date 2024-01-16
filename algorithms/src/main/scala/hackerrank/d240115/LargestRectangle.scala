package hackerrank.d240115

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.annotation.tailrec

/** https://www.hackerrank.com/challenges/largest-rectangle/problem
  */
object LargestRectangle {

  // naive, proper, but slow, N^3
  def largestRectangleN3(hs: Array[Int]): Long = {
    def tryToFit(s: Int, f: Int): Int = (s to f).foldLeft(Int.MaxValue)(_ min hs(_)) * (f - s + 1)
    hs.indices
      .flatMap(i => (i until hs.length).map(j => i -> j))
      .iterator
      .map((tryToFit _).tupled)
      .max
  }

  def largestRectangleDynamic(hs: Array[Int]): Long = {

    /** - stack must contain at least ONE element
      * - stack is always is in growing indexes and heights
      */
    @tailrec
    def cleanStack(m0: Long, is0: List[Int], i0: Int): Long = is0 match {
      case Nil => m0 max hs(i0)

      // was growing or steady
      case i1 :: is if hs(i0) > hs(i1)    =>
        val m1 = hs(i0).toLong * 1
        val m2 = hs(i1).toLong * (i0 - i1 + 1)
        cleanStack(m0 max m1 max m2, is, i0)

      // was steady
      case i1 :: is if hs(i0) == hs(i1)   =>
        val m1 = hs(i1).toLong * (i0 - i1 + 1)
        cleanStack(m0 max m1, is, i0)

      // was declining
      case i1 :: is /* hs(i0) < hs(i1) */ =>
        val m1 = hs(i0).toLong * (i0 - i1 + 1)
        val m2 = minH(i1, i0).toLong * (i0 - i1)
        val mx = m0 max m1 max m2
        cleanStack(mx, is, i0)
    }

    def minH(i1: Int, i0: Int): Int =
//      pprint.pprintln("minH" -> (i1 -> i0))
      (i1 until i0).foldLeft(Int.MaxValue)(_ min hs(_))

    /** - we should never remove last element from stack, since it is used to cleanup the state
      * - the stack is never empty, due to `foldLeft` implementation
      * - ALWAYS: hs(i1) > hs(0)
      */
    def processStack(max0: Long, is0: List[Int], i0: Int): (Long, List[Int]) = is0 match {
      case Nil => sys.error("processStack: impossible by design")

      // here is more complicated thing than minH
      // here we need to do smart processing until the last element,
      // or element less than
      case i1 :: Nil if hs(i1) < hs(i0) =>
        val h0 = minH(i1, i0)
        val w0 = i0 - i1 + 1
        // right
        val m0 = h0.toLong * w0

        val h1 = minH(i1, i0)
        val w1 = i0 - i1
        val m1 = h1.toLong * w1

        val mxA = max0 max m0 max m1
        mxA -> is0 // i0::Nil

      case i1 :: Nil                =>
        val h0 = hs(i0)
        val w0 = i0 - i1 + 1
        // right
        val m0 = h0.toLong * w0

        val h1 = minH(i1, i0)
        val w1 = i0 - i1
        val m1 = h1.toLong * w1

        val mxA = max0 max m0 max m1
        mxA -> is0 // i0::Nil

      // nearest - check 2
      case i1 :: is if i0 - i1 == 1 =>
        val m1 = hs(i0).toLong * (i0 - i1 + 1)
        val m2 = hs(i1).toLong * (i0 - i1)
        val mx = max0 max m1 max m2
        mx -> is0

      case i1 :: is =>
        val m1 = hs(i0).toLong * (i0 - i1 + 1)
        val mx = max0 max m1
        mx -> is0
    }

    hs.indices
      .foldLeft(0L -> List.empty[Int]) {
        case ((mx, Nil), i)                          => mx -> (i :: Nil)
        case ((mx, stack), i) if hs(i) > hs(i - 1)   => mx -> (i :: stack)
        case (state, i) if hs(i) == hs(i - 1)        => state
        case ((mx, stack), i) /* hs(i) < hs(i - 1)*/ => processStack(mx, stack, i)
      } match {
      case (mx, stack) =>
//        pprint.pprintln(stack)
//        pprint.pprintln(hs.length - 1)
        val max = cleanStack(mx, stack, hs.length - 1) // last index
//        pprint.pprintln(max)
        max
    }
  }

}

class LargestRectangleSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {
  import LargestRectangle._

  val testData = Table(
    "heights",
    Array(1, 100, 50),                               //
    Array(1),                                        //
    Array(10),                                       // 10 - steady
    Array(1, 5),                                     // 5  - growing
    Array(1, 2, 3, 4, 5),                            // 9  - growing
    Array(2, 3, 4, 4, 4, 4, 4, 4, 4, 6, 6, 6, 6, 6), // 48 - growing, steady
    Array(5, 5, 5, 5, 3),                            // 20 - steady, declining
    Array(11, 11, 10, 10, 10),                       // 50 - steady, declining
    Array(5, 5, 5, 5, 3, 3),                         // 20 - steady, declining
    Array(6, 5, 4, 3, 2, 1),                         // 12 - declining
    Array(5, 4, 3, 2, 1),                            // 6 - declining
    Array(4, 3, 2, 1),                               // 6 - declining
    Array(3, 3, 2, 2),                               //
    Array(1, 2, 3, 2),                               //
    Array(1, 2, 3, 2, 1),                            //
    Array(1, 2, 3, 2, 1, 1, 1),                      //
    Array(0, 1, 0),                                  //
    Array(1, 0),                                     //
    Array(1, 2, 3, 4, 4, 4, 6, 6, 5),                //
    Array(4, 7, 6, 3),                               //
    Array(4, 7, 6, 4),                               //
    Array(4, 7, 6, 5),                               //
    Array(1, 5, 4),                                  //
    Array(0, 5, 4, 3, 2, 1, 0),                      //
    Array(1, 5, 4, 3, 2, 1),                         //
    Array(1, 5, 4, 3),                               //
    Array(1, 2, 3, 50, 40, 30),                      //
    Array(1, 0, 0),                                  //
//    Array(1, 2, 3, 4, 7, 7, 7, 6, 5, 4, 3, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3),
  )

  test("1") {

    forAll(testData) { hs =>
      pprint.pprintln("hs" -> hs)
      val naive = largestRectangleN3(hs)
      pprint.pprintln("naive" -> naive)
      val dynamic = largestRectangleDynamic(hs)
      pprint.pprintln("dynamic" -> dynamic)
      dynamic shouldBe naive
    }

  }

  test("minimal failing") {
    val g0: Gen[Array[Int]]                = Gen.listOf(Gen.choose(1, 20)).map(_.toArray).filter(_.nonEmpty)
    implicit val a0: Arbitrary[Array[Int]] = Arbitrary(g0)
    forAll { hs: Array[Int] =>
      pprint.pprintln("testing" -> hs.toList)
      val naive   = largestRectangleN3(hs)
      val dynamic = largestRectangleDynamic(hs)
      dynamic shouldBe naive
    }
  }

}
