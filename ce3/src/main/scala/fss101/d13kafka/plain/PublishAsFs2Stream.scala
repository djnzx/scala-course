package fss101.d13kafka.plain

import cats.effect._
import fs2.kafka._
import fss101.d13kafka.KafkaConfiguration

object PublishAsFs2Stream extends IOApp.Simple with KafkaConfiguration {

  val producerSettings =
    ProducerSettings[IO, String, String]
      .withBootstrapServers(kafkaIp)

  def mkRecords(x: Int) =
    ProducerRecords.one(ProducerRecord(topicPlain, s"k$x", s"v$x"))

  val app = KafkaProducer
    .stream[IO, String, String](producerSettings)
    .flatMap { p =>
      fs2.Stream
        .emits(1 to 3)
        .covary[IO]
        .evalMap { x =>
          val r = mkRecords(x)
          p.produce(r)
        }
    }
    .compile
    .drain

  override def run: IO[Unit] = app

}
