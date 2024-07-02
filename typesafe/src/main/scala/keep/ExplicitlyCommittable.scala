package keep

import akka.Done
import akka.kafka.CommitterSettings
import akka.kafka.ConsumerMessage
import akka.kafka.ConsumerMessage.Committable
import akka.kafka.ConsumerMessage.CommittableMessage
import akka.kafka.ConsumerMessage.CommittableOffset
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.ConsumerSettings
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Committer
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.ConsumerRecord
import scala.concurrent.Future

object ExplicitlyCommittable extends App {

  val consumerSettings: ConsumerSettings[String, Int] = ???
  val sub1 = Subscriptions.topics("topic1")

  // it does "offset machinery" under the hood
  val s1: Source[ConsumerRecord[String, Int], Consumer.Control] =
    Consumer.plainSource(consumerSettings, sub1)

  def extract[K, V](msg: CommittableMessage[K, V]): (CommittableOffset, ConsumerRecord[K, V]) =
    msg.committableOffset -> msg.record

  // it gives the api to deal with offsets
  val s2: Source[ConsumerMessage.CommittableMessage[String, Int], Consumer.Control] =
    Consumer.committableSource(consumerSettings, sub1)

  // CommittableOffsetBatch is also "committable"
  def mkBatch(xs: Seq[CommittableOffset]) =
    CommittableOffsetBatch(xs)

  val committerSettings: CommitterSettings = ???
  // to commits
  val commiterFlow: Sink[Committable, Future[Done]] =
    Committer.sink(committerSettings)

}
