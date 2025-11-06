package fss101.d15

import cats.effect._
import fs2.kafka._

object Publish extends IOApp.Simple {

  val kafkaIp = "localhost:9092"
  val topic = "bananas"

  val producerSettings =
    ProducerSettings[IO, String, String]
      .withBootstrapServers(kafkaIp)

  def mkRecord(key: Int, value: String) =
    ProducerRecords.one(ProducerRecord(topic, key.toString, value))

  val app = KafkaProducer
    .stream[IO, String, String](producerSettings)
    .flatMap { p =>
      fs2.Stream
        .emits(2 to 10)
        .covary[IO]
        .evalMap { x =>
          val r = mkRecord(x, s"banana $x")
          p.produce(r)
        }
    }
    .compile
    .drain

  override def run: IO[Unit] = app

}
