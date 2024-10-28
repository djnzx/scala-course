package fss101.d13kafka

import cats.effect.Resource
import cats.effect.Sync
import fs2.kafka.KeyDeserializer
import fs2.kafka.KeySerializer
import fs2.kafka.ValueDeserializer
import fs2.kafka.ValueSerializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

package object vulkan {

  import _root_.vulcan.Codec
  import _root_.vulcan.generic._
  import fs2.kafka.vulcan._

  /** Order */
  sealed trait Status
  object Status {
    case class Placed(date: LocalDateTime) extends Status
    case class Paid(amount: Double)        extends Status
    case class Shipped(by: String)         extends Status
    case class Delivered(at: Long)         extends Status
  }
  case class Order(number: String, amount: Option[Double], status: Status)

  implicit val cLdt: Codec[LocalDateTime] = Codec[Instant]
    .imap { instant =>
      LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    } { ldt =>
      ldt.toInstant(ZoneOffset.UTC)
    }

  implicit val cStatus: Codec[Status] = Codec.derive[Status]
  implicit val cOrder: Codec[Order] = Codec.derive[Order]

  implicit val orderAvroDeserializer: AvroDeserializer[Order] = avroDeserializer
  implicit val orderAvroSerializer: AvroSerializer[Order] = avroSerializer

  /** Car */
  case class Car(brand: String, engine: Int)
  implicit val carCodec: Codec[Car] = Codec.derive
  implicit val carAvroDeserializer: AvroDeserializer[Car] = avroDeserializer
  implicit val carAvroSerializer: AvroSerializer[Car] = avroSerializer

  implicit def mkKeySerializer[F[_]: Sync, A](implicit a: AvroSerializer[A], as: AvroSettings[F]): Resource[F, KeySerializer[F, A]] =
    a.forKey(as)

  implicit def mkValueSerializer[F[_]: Sync, A](implicit a: AvroSerializer[A], as: AvroSettings[F]): Resource[F, ValueSerializer[F, A]] =
    a.forValue(as)

  implicit def mkKeyDeserializer[F[_]: Sync, A](implicit a: AvroDeserializer[A], as: AvroSettings[F]): Resource[F, KeyDeserializer[F, A]] =
    a.forKey(as)

  implicit def mkValueDeserializer[F[_]: Sync, A](implicit a: AvroDeserializer[A], as: AvroSettings[F]): Resource[F, ValueDeserializer[F, A]] =
    a.forValue(as)

}
