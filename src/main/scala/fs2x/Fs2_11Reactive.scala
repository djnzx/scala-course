package fs2x

import fs2._
import fs2.interop.reactivestreams._
import cats.effect.{ContextShift, IO}
import scala.concurrent.ExecutionContext

object Fs2_11Reactive extends App {
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val stream: Stream[IO, Int] = Stream(1, 2, 3).covary[IO]
  // UnicastPublisher must have a single subscriber only
  val publisher: StreamUnicastPublisher[IO, Int] = stream.toUnicastPublisher

}
