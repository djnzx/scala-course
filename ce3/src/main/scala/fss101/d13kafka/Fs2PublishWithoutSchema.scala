package fss101.d13kafka

import cats.effect._
import cats.implicits._
import fs2.kafka._

object Fs2PublishWithoutSchema extends IOApp.Simple {

  val serverIp = "localhost:9092"
  val topicOut = "my-topic-out"

  val producerSettings =
    ProducerSettings[IO, String, String]
      .withBootstrapServers(serverIp)

  def mkRecords(x: Int) =
    ProducerRecords.one(ProducerRecord(topicOut, s"key: $x", s"value: $x"))

  val app = KafkaProducer
    .resource[IO, String, String](producerSettings)
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
