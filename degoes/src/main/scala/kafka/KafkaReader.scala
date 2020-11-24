package kafka

import zio._
import zio.blocking._
import zio.clock.Clock
import zio.duration._
import zio.console._

/**
  * https://medium.com/@oleksandra_a/apache-kafka-and-zio-af418b4c54f0
  */
object KafkaReader {

  import zio.kafka.consumer._
  import zio.kafka.serde._

  val settings: ConsumerSettings =
    ConsumerSettings(List("localhost:9092"))
      .withGroupId("group")
      .withClientId("client")
      .withCloseTimeout(30.seconds)

  val subscription: Subscription = Subscription.topics("topic")

  /** generic blocking reading */
  val readKafka: RIO[Console with Blocking with Clock, Unit] =
    Consumer.consumeWith(settings, subscription, Serde.string, Serde.string) {
      case (key, value) =>
        putStrLn(s"Received message $key: $value")
    }

  /** reading through the environment */
  val readKafkaR: RIO[Console with Blocking with Clock with Consumer, Unit] =
    Consumer.withConsumerService { s =>
      s.consumeWith(subscription, Serde.string, Serde.string) {
        case (key, value) =>
          putStrLn(s"Received message $key: $value")
      }
    }
  
  /** make KAFKA Consumer Service */
  val service: ZManaged[Clock with Blocking, Throwable, Consumer.Service] = Consumer.make(settings)

  /** make ZLayer with KAFKA Consumer Service */
  val layer: ZLayer[Clock with Blocking, Throwable, Consumer.Service] = ZLayer(service)

//  val program = readKafkaR.provideCustomLayer(layer)
  
//  override def run(args: List[String]): URIO[ZEnv, ExitCode] = program.exitCode 
}
