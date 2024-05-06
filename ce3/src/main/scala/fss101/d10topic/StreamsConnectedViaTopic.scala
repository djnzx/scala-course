package fs2x.d10topic

import cats.effect._
import cats.effect.std.Queue
import cats.implicits._
import fs2._
import fs2.concurrent.Topic
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

object StreamsConnectedViaTopic extends IOApp.Simple {

  /** MANY sources can publish message to ONE topic */
  def pub1(t: Topic[IO, Int]) =
    Stream
      .fromIterator[IO](LazyList.from(1).iterator, 1)
      .take(10)
      .metered(1.seconds)
      .evalTap(x => IO.println(s"pub1 emitted: $x"))
//      .through(t.publish) // closes topic after the last element
      .evalTap(t.publish1) // doesn't close topic after the last element

  /** MANY sources can publish message to ONE topic */
  def pub2(t: Topic[IO, Int]) =
    Stream
      .fromIterator[IO](LazyList.from(101).iterator, 1)
      .take(10)
      .metered(2.seconds)
      .evalTap(x => IO.println(s"pub2 emitted: $x"))
      .evalTap(t.publish1)

  /** ALL subscribers get the all items */
  def sub1(t: Topic[IO, Int]) =
    t.subscribeUnbounded
      .evalTap(x => IO.println(s"sub1 consumed: $x"))

  /** ALL subscribers get the all items */
  def sub2(t: Topic[IO, Int]) =
    t.subscribeUnbounded
      .evalTap(x => IO.println(s"sub2 consumed: $x"))

  val program =
    Topic[IO, Int]
      .flatMap(t => Stream(pub1(t), sub1(t), sub2(t)).parJoinUnbounded.compile.drain)

  override def run: IO[Unit] = program
}
