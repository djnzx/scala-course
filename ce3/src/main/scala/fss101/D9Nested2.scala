package fs2x

import cats.effect.{IO, IOApp}

import java.time.{LocalDateTime, ZoneOffset}
import scala.concurrent.duration.DurationInt

object D9Nested2 extends IOApp.Simple {

  override def run: IO[Unit] =
    fs2.Stream
      .emits(1 to 6)
      .covary[IO]
      .parEvalMap(3) { n =>
        IO.realTimeInstant.map(LocalDateTime.ofInstant(_, ZoneOffset.UTC).toLocalTime).flatMap { ld =>
          IO.println(s"$ld $n, sleeping $n seconds") >> IO.sleep(n.seconds)
        }
      }
      .compile
      .drain

}
