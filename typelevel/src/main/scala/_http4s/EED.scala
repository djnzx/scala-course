package _http4s

import cats.effect.Sync
import io.circe.Decoder
import io.circe.Encoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder

trait EED[F[_]] {
  protected implicit def ee[A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf
  protected implicit def ed[A: Decoder](implicit ev: Sync[F]): EntityDecoder[F, A] = jsonOf
}
