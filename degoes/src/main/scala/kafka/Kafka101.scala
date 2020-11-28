package kafka

import zio.blocking.Blocking
import zio.clock.Clock
import zio.duration._
import zio.kafka.consumer.{CommittableRecord, Consumer, ConsumerSettings, OffsetBatch}
import zio.stream.ZStream
import zio._

object Kafka101 extends App {

  /** connector configuration */
  val kafkaConfig: ConsumerSettings = ConsumerSettings(List("localhost:9092"))
  
  /** stream of data read */
  val recordStream: ZStream[Consumer with Clock with Blocking, Throwable, CommittableRecord[String, String]] = 
    ???
//        Consumer.plainStream(Serde.string, Serde.string).flattenChunks

  /** what will we do with our records */
  def processRecords(records: Chunk[CommittableRecord[String, String]]): Task[Unit] = ???

  /** run the stream, can't fail */
  val processingFiber: URIO[Consumer with Clock with Blocking, Fiber.Runtime[Throwable, Unit]] =
    recordStream
      .groupedWithin(1000, 30.seconds)
      .mapM { batch =>
        processRecords(batch) *>
          batch.map(_.offset)
            .foldLeft(OffsetBatch.empty)(_ merge _)
            .commit
      }
      .runDrain
      .fork

  val dataTopicRecords: ZStream[Clock with Blocking with Consumer, Throwable, CommittableRecord[String, String]] =
    ???
//    Consumer.subscribeAnd(Subscription.topics("data-topic"))
//      .plainStream(Serde.string, Serde.string)
//      .flattenChunks
  

  val printerStream = dataTopicRecords
    .mapM { committableRecord =>
      console.putStrLn(committableRecord.record.value)
        .as(committableRecord.offset)
    }
    .aggregateAsync(Consumer.offsetBatches)
//    .mapM(_.commit)

  /** consumer */
  val zioConsumer: ZManaged[Clock with Blocking, Throwable, Consumer.Service] = Consumer.make(kafkaConfig)

  /** consumer layer*/
  val zioConsumerLayer: ZLayer[Clock with Blocking, Throwable, Consumer.Service] = ZLayer(zioConsumer)

  val full: ZLayer[Any, Throwable, Consumer.Service] = Clock.live ++ Blocking.live >>> zioConsumerLayer
  
  val managedStreamFiber =
    processingFiber
//      .provideLayer(zioConsumerLayer)
//      .foreachManaged(_ => ZIO.unit).fork

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ???
}
