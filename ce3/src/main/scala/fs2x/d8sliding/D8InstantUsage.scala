package fss.d8sliding

import cats.effect.IO
import cats.effect.IOApp
import fs2.Pipe
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import scala.concurrent.duration.DurationInt

object D8InstantUsage extends IOApp.Simple {

  val emit = fs2.Stream
    .emits(1 to 5)
    .covary[IO]
    .metered(1.seconds)

  /** hardcoded during compilation, will not change on each call !!! */
  val pipe1: Pipe[IO, Int, LocalTime] = _.as(LocalTime.now)

  /** evaluated each time */
  val pipe2: Pipe[IO, Int, LocalTime] = _.evalMap(_ => IO.realTimeInstant)
    .map(x => LocalDateTime.ofInstant(x, ZoneOffset.ofHours(3)))
    .map(_.toLocalTime)

  val stream1 =
    emit.through(pipe1)

  val stream2 =
    emit.through(pipe2)

  override def run: IO[Unit] =
    stream2
      .evalTap(x => IO(println(x)))
      .compile
      .drain

}
