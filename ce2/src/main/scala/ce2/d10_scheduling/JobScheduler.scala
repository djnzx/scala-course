package ce2.d10_scheduling

import cats.data.Chain
import cats.effect.ContextShift
import cats.effect.ExitCase
import cats.effect.IO
import cats.effect.Resource
import cats.effect.concurrent.Ref
import cats.implicits._
import Reactor.Zzz

trait JobScheduler {
  def schedule(task: IO[_]): IO[Job.Id]
}

object JobScheduler {

  case class State(
      maxRunning: Int,
      scheduled: Chain[Job.Scheduled],
      running: Map[Job.Id, Job.Running],
      completed: Chain[Job.Completed]) {

    def attempt: State = ???

    def onComplete(id: Job.Id, exitCase: ExitCase[Throwable]): State = ???

    def enqueue(job: Job.Scheduled): State = copy(scheduled = scheduled :+ job)

    def dequeue: (State, Option[Job.Scheduled]) =
      if (running.size >= maxRunning) this -> None
      else
        scheduled
          .uncons
          .map { case (h, t) =>
            copy(scheduled = t) -> Some(h)
          }
          .getOrElse(
            this -> None,
          )

    def running(job: Job.Running): State = copy(running = running + (job.id -> job))
  }

  object State {
    def apply(maxRunning: Int): State = State(maxRunning, Chain.nil, Map.empty, Chain.nil)
  }

  def makeScheduler(schedulerState: Ref[IO, State], zzz: Zzz): JobScheduler =
    new JobScheduler {
      override def schedule(task: IO[_]): IO[Job.Id] = for {
        job <- Job.create(task)
        _ <- schedulerState.update(_.enqueue(job))
        _ <- zzz.wakeUp
      } yield job.id
    }

  def resource(maxRunning: Int)(implicit cs: ContextShift[IO]): IO[Resource[IO, JobScheduler]] =
    for {
      schedulerState <- Ref[IO].of(JobScheduler.State(maxRunning))
      zzz <- Zzz.asleep
      scheduler = makeScheduler(schedulerState, zzz)
      reactor = Reactor(schedulerState)
      onStart = (id: Job.Id) => IO.unit
      onComplete = (id: Job.Id, exitCase: ExitCase[Throwable]) => zzz.wakeUp
      loop = (zzz.sleep *> reactor.whenAwake(onStart, onComplete)).foreverM
    } yield loop.background.as(scheduler)

}
