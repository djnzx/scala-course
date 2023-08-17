package http4s

import cats.effect.{IO, IOApp}
import cats.implicits._
import enumeratum.{CirceEnum, Enum, EnumEntry}
import enumeratum.EnumEntry.Hyphencase
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.{HttpRoutes, ParseFailure, QueryParamDecoder, QueryParameterValue}

import scala.concurrent.ExecutionContext.{global => ec}

object Http4sQueryParamDecoderMatcher extends IOApp.Simple {

  sealed trait Color extends EnumEntry with Hyphencase
  object Color extends Enum[Color] with CirceEnum[Color] {
    override def values: IndexedSeq[Color] = findValues
    case object Red extends Color
    case object Green extends Color
    case object Yellow extends Color
    case object Blue extends Color
  }

  implicit val colorParamDecoder: QueryParamDecoder[Color] = (pv: QueryParameterValue) =>
    Color.withNameEither(pv.value).leftMap(e => ParseFailure(e.getMessage(), "")).toValidatedNel

  import SequenceDecoders._
  object OptionalColorParamMatcher extends OptionalQueryParamDecoderMatcher[List[Color]]("color")

  object ListItemsMatcher extends QueryParamDecoderMatcher[List[String]]("items")
  object ItemsMatcher extends QueryParamDecoderMatcher[String]("items")

  val routes = HttpRoutes.of[IO] {

    case GET -> Root / "x" :? ItemsMatcher(items) =>
      pprint.pprintln(items)
      Ok(items)

    case GET -> Root / "xs" :? ListItemsMatcher(items) =>
      pprint.pprintln(items)
      Ok(items)

    case GET -> Root / "a" :? OptionalColorParamMatcher(colors) =>
      pprint.pprintln(colors)
      val folded = colors.fold("option.empty")(_.mkString(":"))
      Ok(folded)

    case rq @ POST -> Root / "c" =>
      TemporaryRedirect.apply(Location(uri"/b"))

    case rq @ POST -> Root / "b" =>
      val bodyF: IO[String] = rq.bodyText.compile.string

      bodyF
        .flatMap(s1 => bodyF.flatMap(s2 => (s1 + s2).pure[IO]))
        .flatMap(Ok(_))
  }

  override def run: IO[Unit] = BlazeServerBuilder[IO]
    .withExecutionContext(ec)
    .bindHttp(8080, "0.0.0.0")
    .withHttpApp(routes.orNotFound)
    .serve
    .compile
    .drain
}
