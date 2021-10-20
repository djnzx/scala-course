package ce2.d10_scheduling

import cats.effect.ContextShift
import cats.effect.ExitCase
import cats.effect.IO
import cats.effect.concurrent.Ref
import cats.implicits._

trait Reactor {
  def whenAwake(
      onStart: Job.Id => IO[Unit],
      onComplete: (Job.Id, ExitCase[Throwable]) => IO[Unit],
    ): IO[Unit]
}

object Reactor {

  sealed trait State
  final case class Asleep() extends State
  final case class Awake() extends State

  def apply(stateRef: Ref[IO, JobScheduler.State])(implicit cs: ContextShift[IO]): Reactor =
    new Reactor {

      def jobCompleted(job: Job.Completed): IO[Unit] =
        stateRef
          .update(_.onComplete(job.id, job.exitCase).attempt)

      def registerOnComplete(job: Job.Running) =
        job
          .await
          .flatMap(jobCompleted)
          .start

      def onStart(id: Job.Id): IO[Nothing] = ???

      def startJob(scheduled: Job.Scheduled): IO[Job.Running] = for {
        running <- scheduled.start
        _ <- stateRef.update(_.addToRunning(running))
        _ <- registerOnComplete(running)
        _ <- onStart(running.id).attempt
      } yield running

      def startNextJob: IO[Option[Job.Running]] = for {
        job <- stateRef.modify(_.dequeue)
        running <- job.traverse(startJob)
      } yield running

      override def whenAwake(
          onStart: Job.Id => IO[Unit],
          onComplete: (Job.Id, ExitCase[Throwable]) => IO[Unit],
        ): IO[Unit] = startNextJob
        .iterateUntil(_.isEmpty)
        .void
    }
}
