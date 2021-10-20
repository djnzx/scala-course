package ce2.d10_scheduling

import cats.data.Chain
import cats.effect.ContextShift
import cats.effect.ExitCase
import cats.effect.IO
import cats.effect.Resource
import cats.effect.concurrent.Ref
import cats.implicits._

trait JobScheduler {
  def schedule(task: IO[_]): IO[Job.Id]
}

object JobScheduler {

  case class State(
      maxRunning: Int,
      scheduled: Chain[Job.Scheduled] = Chain.empty,
      running: Map[Job.Id, Job.Running] = Map.empty,
      completed: Chain[Job.Completed] = Chain.empty) {

    /** enqueue a new job */
    def enqueue(job: Job.Scheduled): State = copy(scheduled = scheduled :+ job)

    /** dequeue a job with some logic */
    def dequeue: (State, Option[Job.Scheduled]) =
      if (running.size >= maxRunning) this -> None
      else
        scheduled.uncons match {
          case Some((h, t)) => copy(scheduled = t) -> Some(h)
          case _            => this -> None
        }

    def addToRunning(job: Job.Running): State = copy(running = running + (job.id -> job))

    def attempt: State = ???

    def onComplete(id: Job.Id, exitCase: ExitCase[Throwable]): State = ???
  }

  def makeScheduler(schedulerState: Ref[IO, State], zzz: Zzz): JobScheduler =
    new JobScheduler {
      override def schedule(task: IO[_]): IO[Job.Id] = for {
        job <- Job.create(task)
        _ <- schedulerState.update(st => st.enqueue(job))
        _ <- zzz.wakeUp // wake up to check whether we can process enqueued job
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
