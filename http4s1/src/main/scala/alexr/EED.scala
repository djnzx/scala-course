package alexr

import cats.effect.IO
import io.circe.Decoder
import io.circe.Encoder
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.circe.jsonOf

trait EED {
  protected implicit def encoder[A: Encoder]: EntityEncoder[IO, A] = jsonEncoderOf
  protected implicit def decoder[A: Decoder]: EntityDecoder[IO, A] = jsonOf
}
