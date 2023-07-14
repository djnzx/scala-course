package http4s1

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object Domain {

  case class Student(age: Int, name: String, message: String)

  object Student {
    implicit val encoder: Encoder[Student] = deriveEncoder
    implicit val decoder: Decoder[Student] = deriveDecoder
    implicit def entityEncoder[F[_]]: EntityEncoder[F, Student] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, Student] = jsonOf
  }

}
