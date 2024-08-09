package fss101.d12sigterm

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Resource.ExitCase
import cats.implicits._
import scala.concurrent.duration.DurationInt

object StreamPreciseTerminationControl extends IOApp.Simple {

  def infiniteStream = fs2.Stream
    .awakeEvery[IO](1.second)
    // .take(4)
    .evalTap(IO.println)
    .drain

  override def run: IO[Unit] =
    infiniteStream
      // .timeout(5.second)
      .onFinalizeCase {
        // precise termination control
        case ExitCase.Succeeded  => IO.println("Succeeded - Just finished")                     // Just finished if finish before timeout
        case ExitCase.Errored(e) => IO.println(s"Errored - With exception (any unhandled): $e") // if finished due to timeout
        case ExitCase.Canceled   => IO.println("Canceled - somehow (maybe SIGTERM/SIGINT)")     // if cancelled
      }
      .compile
      .drain

}
