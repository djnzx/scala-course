package fss101.d15

import cats.effect._
import cats.implicits.toTraverseOps
import cats.syntax.all._
import fs2.kafka._
import org.apache.kafka.common.TopicPartition

import scala.jdk.CollectionConverters.CollectionHasAsScala

object AdminAudit extends IOApp.Simple {

  val kafkaIp = "localhost:9092"

  val consumerSettings = ConsumerSettings[IO, String, Array[Byte]]
    .withBootstrapServers(kafkaIp)

  val adminSettings = AdminClientSettings(kafkaIp)

  def ranges(xs: Set[TopicPartition]) =
    KafkaConsumer
      .resource(consumerSettings)
      .use(x => (x.beginningOffsets(xs), x.endOffsets(xs)).tupled)

  val app = KafkaAdminClient.resource[IO](adminSettings)
    .use { admin =>
      for {
        // 1. topics
        topics0 <- admin.listTopics.names.map(_.toList.sorted)
        topics = topics0.filterNot(_ == "_schemas")
        _       <- IO(pprint.log(topics))

        // 2. partitions per topics
        td <- admin.describeTopics(topics)
        desc = td.fmap(_.partitions.asScala.map(_.partition()))
        _  <- IO(pprint.log(desc))

        // 3. consumerGroups per topics
        consumerGroups <- admin.listConsumerGroups.groupIds
        _              <- IO(pprint.log(consumerGroups))

        // 4. commited offsets
        xs <- consumerGroups.traverse { groupId =>
                admin.listConsumerGroupOffsets(groupId)
                  .partitionsToOffsetAndMetadata.map(_.fmap(_.offset))
              }
        _  <- IO(pprint.log("commited offsets" -> xs.filterNot(_.isEmpty)))

        // 5. beginning / ending offsets
        tps = desc.flatMap { case (t, ps) => ps.map(p => new TopicPartition(t, p)) }.toList.sortBy(_.topic())
        (a, b) <- ranges(tps.toSet)
        combined =
          (a.keySet ++ b.keySet)
            .map(tp => tp -> (a(tp), b(tp))).toList
            .sortBy(tp => tp._1.topic -> tp._1.partition)
        _ <- IO(pprint.log("beginning/ending offsets" -> combined))

      } yield ()
    }

  override def run: IO[Unit] = app
}
