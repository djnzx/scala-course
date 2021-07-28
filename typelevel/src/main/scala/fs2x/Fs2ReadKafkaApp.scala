package fs2x

import cats.effect.{ExitCode, IO, IOApp}
import fs2.kafka.{AutoOffsetReset, CommittableConsumerRecord, ConsumerRecord, ConsumerSettings, KafkaConsumer, KafkaProducer, ProducerRecord, ProducerRecords, ProducerSettings, commitBatchWithin}

import scala.concurrent.duration.DurationInt

object Fs2ReadKafkaApp extends IOApp {

  def processRecord(record: ConsumerRecord[String, String]): IO[(String, String)] =
    IO.pure(record.key -> record.value)

  val consumerSettings: ConsumerSettings[IO, String, String] =
    ConsumerSettings[IO, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withBootstrapServers("kafka-1.dev.sdw.ocs:9092")
      .withGroupId("group")

  val producerSettings: ProducerSettings[IO, String, String] =
    ProducerSettings[IO, String, String]
      .withBootstrapServers("kafka-1.dev.sdw.ocs:9092")

  val stream: fs2.Stream[IO, Unit] =
    KafkaConsumer.stream(consumerSettings)
      .evalTap(_.subscribeTo("sdw.odf.ingest.dev.OG2020"))
      .flatMap(_.stream)
      .mapAsync(25) { ccr: CommittableConsumerRecord[IO, String, String] =>
        processRecord(ccr.record)
          .map { case (k, v) =>
            val newRecord = ProducerRecord("sdw.odf.ingest.dev.OG2020.1", k, v)
            ProducerRecords.one(newRecord, ccr.offset)
          }
      }
      .through(KafkaProducer.pipe(producerSettings))
      .map(_.passthrough)
      .through(commitBatchWithin(500, 15.seconds))

  override def run(args: List[String]): IO[ExitCode] =
    stream
      .compile
      .drain
      .as(ExitCode.Success)

}
