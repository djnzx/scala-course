package fss101.d15

import cats.effect._
import cats.implicits._
import fs2.kafka._

object Fs2ReadKafka extends IOApp.Simple {

  val kafkaIp = "localhost:9092"
  val topic = "fruits"
  val consumerGroupId = "idm-hoth-event-user-opt-out"

  val consumerSettings = ConsumerSettings[IO, String, Array[Byte]]
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withBootstrapServers(kafkaIp)
    .withGroupId(consumerGroupId)

  /** consumer, emitting data from the topic */
  val consumerSubscribed = KafkaConsumer
    .stream(consumerSettings)
    .evalTap(_.subscribeTo(topic))
//    .evalTap(_.assign(topic, NonEmptySet.of(0)))
    .evalTap(_ => IO.println("subscribed"))
    .flatMap(_.stream)

//  val schema294 = Array(0, 0, 0, 1, 38)

  val stream: fs2.Stream[IO, Unit] =
    consumerSubscribed
//      .filter(_.record.key == "5")
      .evalMap { ccr =>
        val s = new String(ccr.record.value)
        IO(pprint.pprintln((ccr.offset, ccr.record.key, s), width = 1000))
//        ccr.offset.commit
      }

  override def run: IO[Unit] = stream.compile.drain

}
