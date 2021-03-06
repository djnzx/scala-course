package fp_red.red15

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec

object SimpleStreamTransducerPlayground5 extends App {

  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._

  val s = (1 to 10).to(Stream)

  /** pack List[Int] to List of len 3 */
  val src: Stream[List[Int]] = List(
    List(1),            // emit: -,               state: (1)
    List(2),            // emit: -,               state: (1,2)
    List(),             // emit: -,               state: (1,2)
    List(3,4,5),        // emit: (1,2,3),         state: (4,5)
    List(6,7,8,9,11,12) // emit: (4,5,6), (7,8,9) state: (11,12)
                        // emit: (11, 12)
  ).to(Stream)

  /** implementation */
  def joinBy[A](n: Int, buf0: List[A], data0: List[A]): (List[List[A]], List[A]) = {

    @tailrec
    def go(bufLen: Int, buf: List[A], data: List[A], acc: List[List[A]] = Nil): (List[List[A]], List[A]) =
      data match {
        /** collected enough, add to the ac */
        case _ if bufLen == n => go(0, Nil, data, acc :+ buf)
        /** data exhausted, return what we have */
        case Nil              => (acc, buf)
        /** size of buffer is less, keep collecting */
        case a :: as          => go(bufLen + 1, buf :+ a, as, acc)
      }

    go(buf0.length, buf0, data0)
  }

  /** attaching to the Streams */
  def repackInto(n: Int): Process[List[Int], List[Int]] =
    loopssq(List.empty[Int]) { (buf, item: List[Int]) =>
      /** function to handle normal join */
      joinBy(n, buf, item)
    } {
      /** function to handle tail state */
      buf => List(buf)
    }

  val r = repackInto(3)(src).toList
  pprint.pprintln(r)
}

class TestJoinSpec extends AnyFunSpec with Matchers {

  import SimpleStreamTransducerPlayground5._

  describe("join") {
    it("1") {
      joinBy(3, List(), List(1,2)) shouldEqual (Nil, List(1,2))
      joinBy(3, List(), List(1,2,3)) shouldEqual (List(List(1,2,3)), Nil)
      joinBy(3, List(1), List(10,20,30)) shouldEqual (List(List(1,10,20)), List(30))
      joinBy(3, List(1), List(3,4,5,6,7,8)) shouldEqual (List(List(1,3,4),List(5,6,7)), List(8))
    }
  }

}

