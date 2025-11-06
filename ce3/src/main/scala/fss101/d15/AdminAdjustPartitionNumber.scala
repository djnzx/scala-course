package fss101.d15

import cats.effect._
import fs2.kafka._
import org.apache.kafka.clients.admin.NewPartitions

object AdminAdjustPartitionNumber extends IOApp.Simple {

  val kafkaIp = "localhost:9092"
  val topic = "bananas"

  val app = KafkaAdminClient
    .resource[IO](AdminClientSettings(kafkaIp))
    .use(_.createPartitions(Map(topic -> NewPartitions.increaseTo(5))))

  override def run: IO[Unit] = app

}
