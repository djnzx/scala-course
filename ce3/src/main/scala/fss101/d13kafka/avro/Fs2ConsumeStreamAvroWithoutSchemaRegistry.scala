package fss101.d13kafka.avro

import _root_.vulcan.Codec
import cats.effect.IO
import cats.effect.IOApp
import fs2.kafka._
import fss101.d13kafka.KafkaConfiguration
import fss101.d13kafka.vulkan.Car
import java.util

object Fs2ConsumeStreamAvroWithoutSchemaRegistry extends IOApp.Simple with KafkaConfiguration {

  /** this deserializer will strip 5 magic bytes
    * from the Kafka payload
    * and decode the content as a plain avro message
    */
  implicit val carDeserializer: ValueDeserializer[IO, Car] =
    GenericDeserializer.instance[IO, Car] {
      (topic, headers, bytes) =>
        IO.delay {
          val payload = util.Arrays.copyOf(bytes, 5)
          Codec[Car].schema
            .flatMap(schema => Codec.fromBinary[Car](payload, schema))
            .fold(e => IO.raiseError[Car](new RuntimeException(e.toString())), IO(_))
        }.flatten
    }

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
