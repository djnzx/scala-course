package http_param_matcher

import cats.implicits.toBifunctorOps
import io.circe.Decoder
import org.http4s.ParseFailure
import org.http4s.QueryParamDecoder

object SealedTraitDecoder {

  private val q       = "\""
  def wrap(s: String) = s"$q$s$q"

  /** - without return type it fails on `2.13.12` with `context-applied` plugin
    *
    * - `QueryParamDecoder[String]` resolves to recursive call
    */
  implicit def customQueryParamDecoder[A: Decoder]: QueryParamDecoder[A] =
    QueryParamDecoder.stringQueryParamDecoder.emap { x =>
      io.circe.parser
        .decode[A](wrap(x))
        .leftMap { ex =>
          ParseFailure(x, ex.getMessage)
        }
    }

}
