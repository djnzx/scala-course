package fs2x.d11fanin

import cats.effect.{IO, IOApp}
import fs2.Stream

object MergingStreams extends IOApp.Simple {

  type F[A] = IO[A]
  case class A()

  /** having */
  val s1: Stream[F, A] = ???
  val s2: Stream[F, A] = ???

  /** creates one stream, interleaving them in order they arrive */
  val both1: Stream[F, A] = s1 merge s2

  /** run two streams in parallel and emit elements once ready */
  val both2: Stream[F, A] = Stream(s1, s2).parJoinUnbounded

  override def run: IO[Unit] = ???
}
