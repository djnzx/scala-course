package t20211602

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.chaining.scalaUtilChainingOps

object Turnstile extends App {

  sealed trait Dir extends Product with Serializable
  case object Enter extends Dir
  case object Exit extends Dir
  object Dir {
    def parse(n: Int) = n match {
      case 0 => Enter
      case 1 => Exit
      case _ => sys.error(s"unexpected value given $n")
    }
  }
  case class St(time: Int, turn: Option[Dir]) {
    def time(t: Int) = copy(time = t)
  }
  object St {
    def apply(t: Int, d: Dir) = new St(t, Some(d))
    def initial = St(Int.MinValue, None)
  }
  
  def getTimes(times: Array[Int], dirs: Array[Int]) = {
    
    /** (Int, Int) means (time, initial position in the array) */
    def go(data: List[((Int, Dir), Int)], state: St, acc: List[(Int, Int)]): List[(Int, Int)] = data match {
      /** 1. list is empty - done */
      case Nil => acc

      /** 2A. a :: b, s.d = a, time is close, needs to be used */
      case ((t1, d1), idx1) :: x :: tail if state.turn.contains(d1) && state.time >= t1 - 1 =>
        val tNext = state.time + 1
        go(x :: tail, state.time(tNext), tNext -> idx1 :: acc)

      /** 2B. a :: b, s.d = b, time is close, needs to be used */
      case x :: ((t2, d2), idx2) :: tail if state.turn.contains(d2) && state.time >= t2 - 1 =>
        val tNext = state.time + 1
        go(x :: tail, state.time(tNext), tNext -> idx2 :: acc)

      /** 3. a :: b, a = b, no state */
      case (a@((t1, d1), idx1)) :: (b@((t2, d2), idx2)) :: tail if t1 == t2 =>
        (d1, d2) match {
          case (d@Exit, _) => go(b :: tail, St(t1, d), t1 -> idx1 :: acc)
          case (_, d@Exit) => go(a :: tail, St(t2, d), t2 -> idx2 :: acc)
        }
        
      /** 4. 1+ item */
      case ((t, d), idx) :: tail =>
        val tNext = (state.time + 1) max t
        go(tail, St(tNext, d), tNext -> idx :: acc)

    }

    (times zip dirs.map(Dir.parse) zipWithIndex).toList
      .pipe(go(_, St.initial, Nil))
      .toArray
      .sortBy(_._2)
      .map(_._1)
  }
}

class TurnstileSpec extends AnyFunSpec with Matchers {

  describe("a") {
    import Turnstile._
    it("1") {
      getTimes(
        Array(0, 0, 1, 5),
        Array(0, 1, 1, 0),
      ) shouldEqual Array(2, 0, 1, 5)
    }

    it("2") {
      getTimes(
        Array(0, 1, 1, 3, 3),
        Array(0, 1, 0, 0, 1),
      ) shouldEqual Array(0, 2, 1, 4, 3)
    }
  }
}
