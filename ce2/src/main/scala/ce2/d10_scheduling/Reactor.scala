package ce2.d10_scheduling

import cats.effect.ContextShift
import cats.effect.ExitCase
import cats.effect.Fiber
import cats.effect.IO
import cats.effect.concurrent.Ref
import cats.implicits._

trait Reactor {

  /** check whether something can be passed to running */
  def whenAwake(
      /** callback */
      onStart: Job.Id => IO[Unit],
      /** callback */
      onComplete: (Job.Id, ExitCase[Throwable]) => IO[Unit],
    ): IO[Unit]
}

object Reactor {

  def apply(stateRef: Ref[IO, JobScheduler.State])(implicit cs: ContextShift[IO]): Reactor =
    new Reactor {
      override def whenAwake(
          onStart: Job.Id => IO[Unit],
          onComplete: (Job.Id, ExitCase[Throwable]) => IO[Unit],
        ): IO[Unit] = {

        /** run this when job completed (just update state) */
        def jobCompleted(job: Job.Completed): IO[Unit] =
          stateRef
            .update(_.onComplete(job))
            .flatTap(_ => onComplete(job.id, job.exitCase).attempt)

        def registerOnComplete(job: Job.Running): IO[Fiber[IO, Unit]] =
          job
            .await
            .flatMap(jobCompleted)
            .start

        def startJob(scheduled: Job.Scheduled): IO[Job.Running] = for {
          /** actually start */
          running <- scheduled.start
          _ <- stateRef.update(_.addToRunning(running))
          _ <- registerOnComplete(running)
          _ <- onStart(running.id).attempt
        } yield running

        /** try to dequeue and start */
        def startNextJob: IO[Option[Job.Running]] = for {
          job <- stateRef.modify(_.dequeue)
          runningJob <- job.traverse(startJob)
        } yield runningJob

        startNextJob
          .iterateWhile(_.nonEmpty)
          .void
      }
    }
}
