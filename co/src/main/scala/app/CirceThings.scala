package app

import io.circe
import io.circe.Decoder
import org.http4s.{ParseFailure, QueryParamDecoder}
import com.cognitops.common.utils.EitherUtils.EitherExtraSyntax
import org.http4s.dsl.io.QueryParamDecoderMatcher

object CirceThings  {
  val q = "\""
  def wrap(s: String) = s"$q$s$q"

  def decodeTypedOrDie[A: Decoder](raw: String) =
    io.circe.parser.decode[A](wrap(raw))
      .fold(_ => sys.error("can't decode"), identity)

  val x = decodeTypedOrDie[Fruit]("Apple")

  implicit val fruitDecoder: QueryParamDecoder[Fruit] =
    QueryParamDecoder[String].emap { x =>
      val wrapped: String = wrap(x)
      val decoded: Either[circe.Error, Fruit] = io.circe.parser.decode[Fruit](wrapped)
      val remapped: Either[ParseFailure, Fruit] = decoded
        .leftMap { ex =>
          ParseFailure(x, ex.getMessage)
        }
      remapped
    }

  object FruitParamMatcher extends QueryParamDecoderMatcher[Fruit]("f")

}
