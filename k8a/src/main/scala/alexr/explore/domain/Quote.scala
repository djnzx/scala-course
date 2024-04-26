package alexr.explore.domain

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class Quote(
    ip: String,
    message: String
)

object Quote {
  implicit val e: Encoder[Quote] = deriveEncoder
  implicit def ee[F[_]]: EntityEncoder[F, Quote] = jsonEncoderOf
}
