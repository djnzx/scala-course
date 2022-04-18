package _http4s

import cats.effect.Sync
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.Decoder
import io.circe.Encoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder

object Domain {

  case class Student(age: Int, name: String, message: String)

  object Student {
    implicit val encoder: Encoder[Student] = deriveEncoder
    implicit val decoder: Decoder[Student] = deriveDecoder
    implicit def entityEncoder[F[_]]: EntityEncoder[F, Student] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, Student] = jsonOf
  }

}
