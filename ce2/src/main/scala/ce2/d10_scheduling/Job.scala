package ce2.d10_scheduling

import cats.effect.concurrent.Deferred
import cats.effect.ContextShift
import cats.effect.ExitCase
import cats.effect.Fiber
import cats.effect.IO

import java.util.UUID

sealed trait Job

object Job {
  case class Id(value: UUID) extends AnyVal

  /** Scheduled */
  case class Scheduled(id: Id, task: IO[_]) extends Job {

    /** Scheduled => Running */
    def start(implicit cs: ContextShift[IO]): IO[Job.Running] = for {
      exitCase <- Deferred[IO, ExitCase[Throwable]]
      fiber <- task
        .void
        .guaranteeCase(exitCase.complete)
        .start
    } yield Job.Running(id, fiber, exitCase)
  }

  /** Running */
  case class Running(
      id: Id,
      fiber: Fiber[IO, Unit],
      exitCase: Deferred[IO, ExitCase[Throwable]])
      extends Job {

    /** Running => Complete */
    def await: IO[Completed] = exitCase
      .get
      .map(ex => Completed(id, ex))
  }

  /** Complete */
  case class Completed(id: Id, exitCase: ExitCase[Throwable]) extends Job

  def create[A](task: IO[A]): IO[Scheduled] =
    IO(Id(UUID.randomUUID())).map(Scheduled(_, task))

}
