package t20211602

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec
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
    def nextTick = time(time + 1)
  }
  object St {
    def apply(t: Int, d: Dir) = new St(t, Some(d))
    def initial = St(Int.MinValue, None)
    def enter(t: Int) = St(t, Some(Enter))
    def exit(t: Int) = St(t, Some(Exit))
  }
  
  def getTimes(times: Array[Int], dirs: Array[Int]) = {
    
    @tailrec
    def go(data: List[((Int, Dir), Int)], state: St, acc: List[(Int, Int)]): List[(Int, Int)] = {
      println("---")
      pprint.pprintln(state)
      pprint.pprintln(data)
      pprint.pprintln(acc.map { case (t, i) => s"t:$t, i:$i"})
      data match {
        /** 1. list is empty - done */
        case Nil =>
          println("Empty")  
          acc

        /** 2. last item, state has close time, we need to take state time */
        case ((t, _), idx) :: Nil if state.time >= t =>
          println("P+S")
          (state.time + 1) -> idx :: acc

        /** 3. last item, just add time */
        case ((t, _), idx) :: Nil  =>
          println("P")
          t -> idx :: acc

        /** 4A. a :: b, s.d = a, time is close, needs to be used */
        case ((t1, d1), idx1) :: x :: tail if state.turn.contains(d1) && state.time >= t1 - 1 =>
          println("a::b, s.d==a")
          val tNext = state.time + 1
          go(x :: tail, state.time(tNext), tNext -> idx1 :: acc)

        /** 4B. a :: b, s.d = b, time is close, needs to be used */
        case x :: ((t2, d2), idx2) :: tail if state.turn.contains(d2) && state.time >= t2 - 1 =>
          println("a::b, s.d==b")
          val tNext = state.time + 1
          go(x :: tail, state.time(tNext), tNext -> idx2 :: acc)

        /** 4C. a :: b, a=b, no state */
        case (a@((t1, d1), idx1)) :: (b@((t2, d2), idx2)) :: tail if t1 == t2 =>
          println("a.t = b.t, no state")
          (d1, d2) match {
            case (Exit, _) => go(b :: tail, St.exit(t1), t1 -> idx1 :: acc)
            case (_, Exit) => go(a :: tail, St.exit(t2), t2 -> idx2 :: acc)
          }

        case ((t, d), idx) :: tail =>
          println("T")
          go(tail, St(t, Some(d)), t -> idx :: acc)
      }
    }

    (times zip dirs.map(Dir.parse) zipWithIndex).toList
      .pipe(go(_, St.initial, Nil))
      .toVector
      .sortBy(_._2)
      .map(_._1)
  }
}

class TurnstileSpec extends AnyFunSpec with Matchers {

  describe("a") {
    import Turnstile._
    it("1") {
      val r = getTimes(
        Array(0,0,1,5),
        Array(0,1,1,0),
      )

      pprint.pprintln(r)
    }

    it("2") {
      val r = getTimes(
        Array(0,1,1,3,3),
        Array(0,1,0,0,1),
      )

      pprint.pprintln(r)
    }
  }
}
