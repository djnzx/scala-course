package rtj_ce.part4coordination

import cats.effect.IO
import cats.effect.Ref
import cats.effect.kernel.Deferred
import scala.collection.immutable.Queue

/** cancellable mutex */
trait C3Mutex1 {
  def acquire: IO[Unit]
  def release: IO[Unit]
}

object C3Mutex1 {
  type Signal = Deferred[IO, Unit]
  case class State(locked: Boolean, waiting: Queue[Signal])
  val unlocked: State = State(locked = false, Queue.empty)

  def createSignal(): IO[Signal] = Deferred[IO, Unit]

  def create: IO[C3Mutex1] = Ref[IO].of(unlocked).map(createMutexWithCancellation)

  def createMutexWithCancellation(state: Ref[IO, State]): C3Mutex1 =
    new C3Mutex1 {
      override def acquire = IO.uncancelable { poll =>
        createSignal().flatMap { signal =>
          val cleanup = state.modify { case State(locked, queue) =>
            val newQueue = queue.filterNot(_ eq signal)
            State(locked, newQueue) -> release
          }.flatten

          state.modify {
            case State(false, _)    => State(locked = true, Queue())               -> IO.unit
            case State(true, queue) => State(locked = true, queue.enqueue(signal)) -> poll(signal.get).onCancel(cleanup)
          }.flatten // modify returns IO[B], our B is IO[Unit], so modify returns IO[IO[Unit]], we need to flatten
        }
      }

      override def release = state.modify {
        case State(false, _)    => unlocked -> IO.unit
        case State(true, queue) =>
          if (queue.isEmpty) unlocked -> IO.unit
          else {
            val (signal, rest) = queue.dequeue
            State(locked = true, rest) -> signal.complete(()).void
          }
      }.flatten
    }
}
