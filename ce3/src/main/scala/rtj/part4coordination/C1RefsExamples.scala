package rtj.part4coordination

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import cats.syntax.parallel._
import utils._

object C1RefsExamples extends IOApp.Simple {

  def demoConcurrentWorkImpure(): IO[Unit] = {
    var count = 0

    def task(workload: String): IO[Unit] = {
      val wordCount = workload.split(" ").length
      for {
        _ <- IO(s"Counting words for '$workload': $wordCount'").debug
        newCount <- IO(count + wordCount)
        _ <- IO(s"New total: $newCount").debug
        _ <- IO(count += wordCount)
      } yield ()
    }

    List("I love Cats Effect", "This ref thing is useless", "Daniel writes a lot of code")
      .map(task)
      .parSequence
      .void
  }
  /*
    Drawbacks:
    - hard to read/debug
    - mix pure/impure code
    - NOT THREAD SAFE
   */

  def demoConcurrentWorkPure(): IO[Unit] = {
    def task(workload: String, total: Ref[IO, Int]): IO[Unit] = {
      val wordCount = workload.split(" ").length

      for {
        _ <- IO(s"Counting words for '$workload': $wordCount'").debug
        newCount <- total.updateAndGet(currentCount => currentCount + wordCount)
        _ <- IO(s"New total: $newCount").debug
      } yield ()
    }

    for {
      initialCount <- Ref[IO].of(0)
      _ <- List("I love Cats Effect", "This ref thing is useless", "Daniel writes a lot of code")
        .map(string => task(string, initialCount))
        .parSequence
    } yield ()
  }

  override def run: IO[Unit] =
    demoConcurrentWorkPure()
//    demoConcurrentWorkImpure()
}
