package app

import io.circe.Decoder
import org.http4s.{ParseFailure, QueryParamDecoder}
import com.cognitops.common.utils.EitherUtils.EitherExtraSyntax

object SealedTraitDecoder {

  private val q = "\""
  def wrap(s: String) = s"$q$s$q"

  implicit def customQueryParamDecoder[A: Decoder] =
    QueryParamDecoder[String].emap { x =>
      io.circe.parser.decode[A](wrap(x))
        .leftMap { ex =>
          ParseFailure(x, ex.getMessage)
        }
    }

}
