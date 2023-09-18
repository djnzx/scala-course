package streaming

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

/** ReactivePlayground
  *
  * - consuming from org.reactivestreams.Publisher[A]
  *     to Akka Source
  *
  * - publishing to org.reactivestreams.Publisher[A]
  *     Akka Sink implementation
  */
class AkkaStreamsReactivePlayground[A] {

  implicit val materializer: Materializer = ???

  /** having any library providing source as a [[org.reactivestreams.Publisher]] */
  val elasticSearchPublisher: Publisher[A] = ???

  /** we can easily convert it to Akka Stream */
  val akkaStream: Source[A, NotUsed] = Source.fromPublisher(elasticSearchPublisher)

  /** having any library providing source as a [[org.reactivestreams.Subscriber]] */
  val elasticSearchSubscriber: Subscriber[A] = ???

  /** we can create a [[akka.stream.scaladsl.Sink]] */
  val sink: Sink[A, NotUsed] = Sink.fromSubscriber(elasticSearchSubscriber)

  val r: NotUsed = akkaStream.to(sink).run

}
