package fss101.d13kafka.plain

import cats.effect.IO
import cats.effect.IOApp
import fs2.kafka.AutoOffsetReset
import fs2.kafka.ConsumerSettings
import fs2.kafka.KafkaConsumer
import fss101.d13kafka.KafkaConfiguration

object ConsumeFs2Stream extends IOApp.Simple with KafkaConfiguration {

  val consumerSettings = ConsumerSettings[IO, String, String]
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withBootstrapServers(kafkaIp)
    .withGroupId(consumerGroupId)

  val consumerSubscribed =
    KafkaConsumer
      .stream(consumerSettings)
      .evalTap(_.subscribeTo(topicPlain))
      .flatMap(_.stream)

  override def run: IO[Unit] =
    consumerSubscribed
      .evalTap(x => IO.blocking(println(x)))
      .compile
      .drain
}
