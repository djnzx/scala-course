package fss101.d13kafka.avro

import cats.effect.{IO, IOApp}
import fs2.kafka.{AutoOffsetReset, ConsumerSettings, KafkaConsumer}
import fs2.kafka.vulcan.{AvroSettings, SchemaRegistryClientSettings}
import fss101.d13kafka.KafkaConfiguration
import fss101.d13kafka.vulkan.Car

object Fs2ConsumeStreamAvro extends IOApp.Simple with KafkaConfiguration {

  val schemaRegistrySettings = SchemaRegistryClientSettings[IO](schemaRegistryUrl)
  implicit val avroSettings = AvroSettings[IO](schemaRegistrySettings)

  val consumerSettings = ConsumerSettings[IO, String, Car]
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withBootstrapServers(kafkaIp)
    .withGroupId(consumerGroupId)

  val consumerSubscribed =
    KafkaConsumer
      .stream(consumerSettings)
      .evalTap(_.subscribeTo(topicAvro))
      .flatMap(_.stream)

  override def run: IO[Unit] =
    consumerSubscribed
      .evalTap(x => IO.blocking(println(x)))
      .compile
      .drain

}
