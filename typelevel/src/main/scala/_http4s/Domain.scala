package _http4s

import cats.effect.{ExitCase, Sync}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object Domain {

  case class Student(age: Int, name: String, message: String)

  object Student {
    implicit val encoder: Encoder[Student] = deriveEncoder
    implicit val decoder: Decoder[Student] = deriveDecoder
    implicit def entityEncoder[F[_]]: EntityEncoder[F, Student] = jsonEncoderOf
    implicit def entityDecoder[F[_] : Sync]: EntityDecoder[F, Student] = jsonOf
  }

}
