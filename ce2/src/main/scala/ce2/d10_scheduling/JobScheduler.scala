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

    /** move from Running => Completed */
    def onComplete(job: Job.Completed): State =
      copy(running = running - job.id, completed = completed :+ job)

  }

  private def makeInitialState(n: Int): IO[Ref[IO, State]] = Ref[IO].of(JobScheduler.State(n))

  private def makeScheduler(schedulerState: Ref[IO, State], zzz: Zzz): JobScheduler =
    new JobScheduler {
      override def schedule(task: IO[_]): IO[Job.Id] = for {
        job <- Job.create(task)
        _ <- schedulerState.update(st => st.enqueue(job))
        _ <- zzz.wakeUp // wake up to check whether we can process enqueued job
      } yield job.id
    }

  def resource(maxRunning: Int)(implicit cs: ContextShift[IO]): IO[Resource[IO, JobScheduler]] =
    for {
      schedulerState <- makeInitialState(maxRunning)
      zzz <- Zzz.asleep // initially sleep
      scheduler = makeScheduler(schedulerState, zzz)
      reactor = Reactor(schedulerState)
      onStart = (id: Job.Id) => IO(println(s"job $id started")) >> IO.unit
      onComplete = (id: Job.Id, exitCase: ExitCase[Throwable]) =>
        IO(println(s"job $id finished with state $exitCase")) >> zzz.wakeUp
      loop = (zzz.sleep *> reactor.whenAwake(onStart, onComplete)).foreverM
    } yield loop.background.as(scheduler)

}
