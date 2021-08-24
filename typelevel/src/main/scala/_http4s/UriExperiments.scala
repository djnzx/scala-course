package _http4s

import cats.Defer
import cats.Monad
import cats.data.ValidatedNel
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import org.http4s._
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io._
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import java.time.Year
import java.util.UUID
import scala.concurrent.duration.DurationInt
import scala.util.Try

object UriExperiments extends App {

  case class Director(firstNme: String, lastName: String)
//  implicit val yearDecoder: QueryParamDecoder[Year] = QueryParamDecoder[Int].map(y => Year.of(y))
  implicit val yearDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int]
      .emap { y =>
        Try { Year.of(y) }
          .toEither
          .leftMap { e =>
            ParseFailure(e.getMessage, e.getMessage)
          }
      }

  object IdParamMatcher extends QueryParamDecoderMatcher[Int]("id")
  object YearParamMatcher extends OptionalQueryParamDecoderMatcher[Year]("year")
  object YearParamMatcherValidate extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")
  object DirectorExtractor {
    def unapply(s: String): Option[Director] = s match {
      case s"$first $last" => Some(Director(first, last))
      case _               => None
    }
  }
  implicit def intEncoder[F[_]]: EntityEncoder[F, Int] = jsonEncoderOf[F, Int]
  implicit def strEncoder[F[_]]: EntityEncoder[F, String] = jsonEncoderOf[F, String]
  implicit def uuidEncoder[F[_]]: EntityEncoder[F, UUID] = jsonEncoderOf[F, UUID]

  def routes1[F[_]: Monad: Defer]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      // partial function
      case GET -> Root / "path1" :? YearParamMatcherValidate(year) =>
        val x: Option[ValidatedNel[ParseFailure, Year]] = year
        ???
      case GET -> Root / "path1" :? IdParamMatcher(id) +& YearParamMatcher(year) => Ok.apply(id)
      case GET -> Root / "path2" / UUIDVar(movieId) :? IdParamMatcher(id)        => Response[F](Ok).withEntity(movieId).pure[F]
      case GET -> Root / "path2" / UUIDVar(movieId) :? IdParamMatcher(id)        => NotFound(id)
    }
  }

  def routes2[F[_]: Monad: Defer]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "path3" / DirectorExtractor(director)            => ???
      case GET -> Root / "path4" / UUIDVar(movieId) :? IdParamMatcher(id) => ???
    }
  }

  def allRoutes1[F[_]: Monad: Defer]: HttpRoutes[F] =
    routes1[F] <+> routes2[F]

  def complete[F[_]: Monad: Defer] =
    allRoutes1[F].orNotFound

  def allRoutes2[F[_]: Monad: Defer]: HttpRoutes[F] = Router(
    "a" -> routes1,
    "b" -> routes2,
  )

  implicit val intParamEncoder: EntityEncoder[IO, Int] = jsonEncoderOf

  def rq1(id: Int) = Request[IO](
    Method.GET,
    uri"path1".withQueryParam("id", id),
  )

  def rq2(id: Int) = Request[IO](
    Method.GET,
    (uri"a" / "path2").withQueryParam("id", id),
  )

  object RequestUriSyntax {

    implicit class UriAddParentSyntax(private val uri: Uri) extends AnyVal {
      def prepend(parentUri: Uri): Uri = parentUri.resolve(uri)
    }

    implicit class RequestUriAddParentSyntax[F[_]](private val rq: Request[F]) extends AnyVal {
      def prependUri(parentUri: Uri): Request[F] = rq.withUri(rq.uri.prepend(parentUri))
    }

  }

  /** 1. we have a request */
  val rq111: Request[IO] = rq1(111) // uri=path1?id=111

  /** 2. we have a parent URI */
  val parentUri = uri"parent/"

  /** 3.1. without syntax - we need to write: */
  val rq111a = rq111.withUri(parentUri.resolve(rq111.uri)) // uri=parent/path1?id=111

  /** 3.2 having one import in scope we have the way more clean syntax */
  import RequestUriSyntax.RequestUriAddParentSyntax
  val rq111b = rq111.prependUri(parentUri) // uri=parent/path1?id=111

  println(rq111a)
  println(rq111b)
//  val r =
//    routes1(rq1(33))
//      // actually run
//      .unsafeRunSync()
//      .as[String]
//      // access body
//      .unsafeRunSync()
//
//  println(r)

//  val r2 = allRoutes(rq2(66))
//    .value
//    .unsafeRunSync()
//    .get
//    .as[String]
//    .unsafeRunSync()
//
//  println(r2)

//  override def run(args: List[String]): IO[ExitCode] = {
//
//    val router: HttpApp[IO] = Router(
//      "/api" -> routes1[IO],
//    ).orNotFound
//
//    BlazeServerBuilder[IO](executionContext)
//      .bindHttp(8080, "localhost")
//      .withHttpApp(router)
//      .resource
//      .use(_ => IO.never.timeout(10.seconds))
//      .as(ExitCode.Success)

//  }
}
