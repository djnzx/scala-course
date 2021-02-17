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
  
  case class State(time: Int, turn: Option[Dir])
  object State {
    def initial = State(Int.MinValue, None)
    def enter(t: Int) = State(t, Some(Enter))
    def exit(t: Int) = State(t, Some(Exit))
  }
  
  def getTimes(times: Array[Int], dirs: Array[Int]) = {
    
    @tailrec
    def go(data: List[(Int, Dir)], state: State, acc: List[Int]): List[Int] = data match {
      // list is empty - done
      case Nil => acc
      
      // last item, just add, probably TODO: NEED TO CHECK THE STATE (whether to increment time)
      case (t, _) :: Nil => t :: acc
      
      /** more than one with two items with the same time, need to analyze the state */
      case (t1, d1) :: (t2, d2) :: tail if (t1 == t2) && (state.time == t1 - 1) => ???
      
      /** more than one with two items with the same time, state doesn't need to be used */
      case (a @ (t1, d1)) :: (b @ (t2, d2)) :: tail if t1 == t2 => 
        (d1, d2) match {
          case (Enter, Exit) => go(a :: tail, State.exit(t2), t2 :: acc)
          case (Exit, Enter) => go(b :: tail, State.exit(t2), t1 :: acc)
        }
      /** more than one, time is different, so doesn't matter */
      case (t, d) :: tail => go(tail, State(t, Some(d)), t :: acc)
    }

    (times zip dirs.map(Dir.parse)).toList
      .pipe(go(_, State.initial, Nil))
      .reverse
  }
}

class TurnstileSpec extends AnyFunSpec with Matchers {

  describe("a") {
    it("1") {
    }
  }
}
