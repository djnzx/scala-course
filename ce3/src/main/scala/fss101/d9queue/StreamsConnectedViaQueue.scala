package fs2x.d9queue

import cats.effect._
import cats.effect.std.Queue
import cats.implicits._
import fs2._
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

object StreamsConnectedViaQueue extends IOApp.Simple {

  def pub(q: Queue[IO, Option[Int]]) =
    (Stream
      .fromIterator[IO](LazyList.from(1).iterator, 1)
      .take(6)
      .metered(3.seconds)
      .map(Some(_)) ++ Stream(None))
      .evalTap(x => IO.println(s"PUB emitted: $x"))
      .evalTap(q.offer)

  def sub(q: Queue[IO, Option[Int]]) =
    Stream
      .fromQueueNoneTerminated(q)
      .evalTap(x => IO.println(s"SUB consumed: $x"))

  val program =
    Queue
      .unbounded[IO, Option[Int]]
      .flatMap(q => Stream(pub(q), sub(q)).parJoinUnbounded.compile.drain)

  override def run: IO[Unit] = program
}
