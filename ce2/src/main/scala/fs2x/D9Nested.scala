package fs2x

import cats.effect.Clock
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxFlatMapOps
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.DurationInt

object D9Nested extends IOApp.Simple {

  override def run: IO[Unit] =
    fs2.Stream
      .emits(1 to 6)
      .covary[IO]
      .parEvalMap(3) { n =>
        Clock[IO].realTime(TimeUnit.SECONDS).flatMap { ld =>
          IO(println(s"$ld $n, sleeping $n seconds")) >> IO.sleep(n.seconds)
        }
      }
      .compile
      .drain

}
