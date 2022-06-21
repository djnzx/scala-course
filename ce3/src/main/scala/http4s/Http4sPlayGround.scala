package http4s

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import enumeratum.CirceEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase
import org.http4s.HttpRoutes
import org.http4s.ParseFailure
import org.http4s.QueryParamDecoder
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.implicits._
import scala.concurrent.ExecutionContext.{global => ec}

object Http4sPlayGround extends IOApp.Simple {

  sealed trait Color extends EnumEntry with Hyphencase
  object Color extends Enum[Color] with CirceEnum[Color] {
    override def values: IndexedSeq[Color] = findValues
    case object Red extends Color
    case object Green extends Color
    case object Yellow extends Color
    case object Blue extends Color
  }

  implicit val colorParamDecoder: QueryParamDecoder[Color] = pv =>
    Color.withNameEither(pv.value).leftMap(e => ParseFailure(e.getMessage(), "")).toValidatedNel

  import SequenceDecoders._
  object OptionColorParamMatcher extends OptionalQueryParamDecoderMatcher[Set[Color]]("color")

  val routes = HttpRoutes.of[IO] { case GET -> Root / "a" :? OptionColorParamMatcher(colors) =>
    val folded = colors.fold("option.empty")(_.mkString("."))
    Ok(folded)
  }

  override def run: IO[Unit] = BlazeServerBuilder[IO]
    .withExecutionContext(ec)
    .bindHttp(8080, "0.0.0.0")
    .withHttpApp(routes.orNotFound)
    .serve
    .compile
    .drain
}
