package fss101.d12sigterm

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import scala.concurrent.duration.DurationInt

object StreamTerminationFinalizer extends IOApp.Simple {

  def infiniteStream = fs2.Stream
    .awakeEvery[IO](1.second)
//    .take(4)
    .evalTap(IO.println)
    .drain

  override def run: IO[Unit] =
    infiniteStream
//      .timeout(5.second)
      .onFinalize(IO.println("finally...")) // in the end of the stream regardless the case
      .compile
      .drain

}
