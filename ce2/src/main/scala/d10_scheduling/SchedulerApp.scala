package d10_scheduling

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object SchedulerApp extends IOApp {

  def makeJob(id: Int, sleepTime: FiniteDuration): IO[Unit] =
    for {
      _ <- IO { println(s"Job $id started\nwill be sleeping for $sleepTime") }
      _ <- IO.sleep(sleepTime)
      _ <- IO { println(s"Job $id finished") }
    } yield ()

  val jobs: Seq[IO[Unit]] = (1 to 10).map(id => makeJob(id, (id % 5).seconds))

  val app: IO[Unit] = for {
    resource <- JobScheduler.resource(maxRunning = 2)
    _ <- resource.use { s: JobScheduler =>
      jobs.foreach { job: IO[Unit] =>
        s.schedule(job)
      }
      IO.unit
    }
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
