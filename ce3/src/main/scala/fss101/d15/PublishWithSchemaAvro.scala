package fss101.d15

import cats.effect._
import fs2.kafka._
import fs2.kafka.vulcan.{AvroSettings, SchemaRegistryClientSettings}
import fss101.d13kafka.KafkaConfiguration
import fss101.d13kafka.vulkan.Car

object PublishWithSchemaAvro extends IOApp.Simple with KafkaConfiguration {

  val schemaRegistrySettings = SchemaRegistryClientSettings[IO](schemaRegistryUrl)
  implicit val avroSettings = AvroSettings[IO](schemaRegistrySettings)

  val producerSettings =
    ProducerSettings[IO, String, Car]
      .withBootstrapServers(kafkaIp)

  def mkRecords(x: Int) =
    ProducerRecords.one(ProducerRecord(topicAvro, s"k$x", Car(s"A$x", x)))

  val app = KafkaProducer
    .resource[IO, String, Car](producerSettings)
    .use { p =>
      fs2.Stream
        .emits(1 to 3)
        .covary[IO]
        .evalMap { x =>
          val r = mkRecords(x)
          p.produce(r)
        }
        .compile
        .drain
    }

  override def run: IO[Unit] = app

}
