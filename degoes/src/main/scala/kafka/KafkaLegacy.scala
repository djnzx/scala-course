package kafka

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import zio.blocking.Blocking
import zio.duration._
import zio.{Fiber, Task, ZIO, blocking}

/**
  * SYNC...
  * NON MANAGEABLE
  */
object KafkaLegacy {
  
  val consumer: KafkaConsumer[String, String] = ???

  def processRecords(records: ConsumerRecords[String, String]): Task[Unit] = ???

  val consumerFiber: ZIO[Blocking, Nothing, Fiber[Throwable, Nothing]] =
    (for {
      data <- blocking.effectBlocking(consumer.poll(50.millis.asJava))
      _    <- processRecords(data)
      _    <- blocking.effectBlocking(consumer.commitSync())
    } yield ()).forever.fork

}

