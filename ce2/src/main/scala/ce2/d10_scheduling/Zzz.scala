package ce2.d10_scheduling

import cats.effect.concurrent.Ref
import cats.effect.IO

trait Zzz {
  def sleep: IO[Unit]
  def wakeUp: IO[Unit]
}

object Zzz {

  sealed trait State
  case object Awake extends State
  case object Sleep extends State

  /** sleeping instance */
  def asleep: IO[Zzz] = IO {
    new Zzz {
      val state: IO[Ref[IO, State]] = Ref[IO].of[State](Sleep)

      override def sleep: IO[Unit] = for {
        s <- state
        _ <- s.update {
          case Awake => Sleep
          case s @ _ => s
        }
      } yield ()

      override def wakeUp: IO[Unit] = for {
        s <- state
        _ <- s.update {
          case Sleep => Awake // TODO: run handler here
          case a @ _ => a
        }
      } yield ()
    }
  }
}
