package rtj.part4coordination

import cats.effect.IO
import cats.effect.Ref
import cats.effect.kernel.Deferred

/** Exercise: implement your own C5CDLatch with Ref and Deferred. */

trait C5CDLatch {
  def await: IO[Unit]
  def release: IO[Unit]
}

object C5CDLatch {

  /** it has internal state */
  sealed trait State
  case object Done extends State
  case class Live(remainingCount: Int, signal: Deferred[IO, Unit]) extends State

  /** it's being created inside apply */
  def apply(count: Int): IO[C5CDLatch] = for {
    signal <- Deferred[IO, Unit]
    state <- Ref[IO].of[State](Live(count, signal))
  } yield new C5CDLatch {

//    override def await = state.get.flatMap { s: State =>
//      if (s == Done) IO.unit // continue, the latch is dead
//      else signal.get // block here
//    }

    override def await = state.get.flatMap {
      case Done => IO.unit // continue, the latch is dead
      case _    => signal.get // block here
    }

    override def release = state
      .modify {
        case Done            => Done                -> IO.unit
        case Live(1, signal) => Done                -> signal.complete(()).void
        case Live(n, signal) => Live(n - 1, signal) -> IO.unit
      }
      .flatten
      .uncancelable
  }
}
