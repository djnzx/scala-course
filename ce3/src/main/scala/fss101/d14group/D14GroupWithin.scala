package fss101.d14group

import cats.effect.{IO, IOApp}
import fs2.Pipe

import java.time.{LocalDateTime, LocalTime, ZoneOffset}
import scala.concurrent.duration.DurationInt

object D14GroupWithin extends IOApp.Simple {

  val emit = fs2.Stream
    .emits(1 to 5)
    .covary[IO]
    .metered(5.seconds)

  override def run: IO[Unit] =
    emit
      .evalTap(x => IO(pprint.log("emitted" -> x)))
      // it will not produce empty chunks
      .groupWithin(10, 1.second)
      .evalTap(xs => IO(pprint.log(xs)))
      .drain
      .compile
      .drain

}
