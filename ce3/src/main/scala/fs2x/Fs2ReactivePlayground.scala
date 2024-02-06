package fs2x

import cats.effect.Async
import fs2.interop.reactivestreams._
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

import java.util.concurrent.Flow

/** ReactivePlayground
  *
  * - consuming from org.reactivestreams.Publisher[A]
  *     to fs2.Stream[F, A]
  *
  * - publishing to org.reactivestreams.Publisher[A]
  *     via fs2Stream.subscribe(elasticSearchSubscriber)
  */
class Fs2ReactivePlayground[F[_]: Async, A] {

  /** having any library providing source as a [[org.reactivestreams.Publisher]] */
  val elasticSearchPublisher: Publisher[A] = ???

  /** we can easily convert it to fs2Stream */
  val fs2Stream: fs2.Stream[F, A] = fromPublisher[F, A](elasticSearchPublisher, 1000)

  /** having any library providing source as a [[org.reactivestreams.Subscriber]] */
  val elasticSearchSubscriber: Flow.Subscriber[A] = ???

  /** we can sink top it */
  val r = fs2Stream.subscribe(elasticSearchSubscriber)

}
