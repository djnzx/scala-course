package rtj.part4coordination

import cats.effect.kernel.Deferred
import cats.effect.IO
import cats.effect.Ref

import scala.collection.immutable.Queue

/** simple mutex */
trait C3Mutex0 {
  def acquire: IO[Unit]
  def release: IO[Unit]
}

object C3Mutex0 {
  private type Signal = Deferred[IO, Unit]
  private case class State(locked: Boolean, waiting: Queue[Signal])
  private val unlocked: State = State(locked = false, Queue.empty)
  private val EMPTY = Queue.empty[Signal]

  private def createSignal(): IO[Signal] = Deferred[IO, Unit]

  /** newly created mute is always unlocked with empty queue */
  def create: IO[C3Mutex1] = Ref[IO].of(unlocked).map(createSimpleMutex)

  private def createSimpleMutex(state: Ref[IO, State]): C3Mutex1 = new C3Mutex1 {

    override def acquire = createSignal().flatMap { signal =>
      state.modify {
        /** mutex wasn't locked => lock it */
        case State(false, _) => State(locked = true, Queue.empty) -> IO.unit
        /** mutex was locked => enqueue request */
        case State(true, q) => State(locked = true, q.enqueue(signal)) -> signal.get
      }.flatten // modify returns IO[B], our B is IO[Unit], so modify returns IO[IO[Unit]], we need to flatten
    }

    override def release = state.modify {
      /** unlocked => keep unlocked */
      case State(false, _) => unlocked -> IO.unit
      /** locked with empty queue => unlock */
      case State(true, EMPTY) => unlocked -> IO.unit
      /** locked with NON-empty queue => keep locked, send complete, dequeue */
      case State(true, queue) =>
        val (signal, tail) = queue.dequeue
        State(locked = true, tail) -> signal.complete(()).void
    }.flatten
  }
}
