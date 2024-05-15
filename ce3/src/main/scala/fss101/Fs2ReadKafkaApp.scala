package fs2x

import cats.effect._
import cats.implicits._
import fs2._
import fs2.kafka.CommittableOffset
import fs2.kafka.ProducerRecords
import fs2.kafka._
import java.util.UUID
import scala.concurrent.duration.DurationInt

/** https://fd4s.github.io/fs2-kafka/docs/quick-example */
object Fs2ReadKafkaApp extends IOApp.Simple {

  val serverIp = "kafka.bla-bla-bla:9092"
  val consumerGroupId = "ingest-".concat(UUID.randomUUID().toString)
  val topicIn = "my-topic-in"
  val topicOut = "my-topic-out"

  /** identity here just to explore the syntax */
  def processRecord(r: ConsumerRecord[String, String]): IO[(String, String)] =
    (r.key -> r.value).pure[IO]

  // TODO: idea: pass `generic (untyped) consumer as a parameter`
  //  and reuse generic consumer
  def mkTypedConsumer[F[_], K, V](
    keyDeserializer: KeyDeserializer[F, K],
    valueDeserializer: ValueDeserializer[F, V]
  ): ConsumerSettings[F, K, V] =
    ConsumerSettings
      .apply[F, K, V](keyDeserializer, valueDeserializer)
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withBootstrapServers(serverIp)
      .withGroupId(consumerGroupId)

  val consumerSettings = ConsumerSettings[IO, String, String]
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withBootstrapServers(serverIp)
    .withGroupId(consumerGroupId)

  val producerSettings = ProducerSettings[IO, String, String]
    .withBootstrapServers(serverIp)

  /** consumer, emitting data from the topic */
  val consumerSubscribed: Stream[IO, CommittableConsumerRecord[IO, String, String]] = KafkaConsumer
    .stream(consumerSettings)
    .evalTap(_.subscribeTo(topicIn))
    .flatMap(_.stream)

  /** stream of 1 element with producer, for easier composition */
  val producerS = KafkaProducer.stream(producerSettings)

  //                         -- offset did read --  ----- records to publish -----     -- offset did read --  ------ records published ------
  val producePipe: Pipe[IO, (CommittableOffset[IO], ProducerRecords[String, String]), (CommittableOffset[IO], ProducerResult[String, String])] =
    in =>
      producerS.flatMap { producer: KafkaProducer[IO, String, String] =>
        in
          .evalMap { case (offset, toProduce) =>
            producer
              .produce(toProduce) // we will flatten concurrently later to improve performance
              .map(produced => offset -> produced)
          }
          .parEvalMap(16) { case (commit, resultF) =>
            resultF.map(r => commit -> r)
          }
      }

  //                       ----- to commit ----                           records or seconds, what come first
  val commitPipe: Pipe[IO, CommittableOffset[IO], Unit] = kafka.commitBatchWithin[IO](500, 15.seconds)

  val stream: fs2.Stream[IO, Unit] =
    consumerSubscribed
      // parallelism for processing
      .mapAsync(16) { committable =>
        // K = ccr.record.key
        // V = ccr.record.value
        processRecord(committable.record)
          .map { case (k, v) =>
            val toPublish = ProducerRecord(topicOut, k, v)
            committable.offset -> ProducerRecords.one(toPublish)
          }
      }
      .through(producePipe)
      .map { case (offset, produced) => offset }
      .through(commitPipe)

  override def run: IO[Unit] = stream.compile.drain

}
