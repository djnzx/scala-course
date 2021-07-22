package _http4s

import cats.effect.{ExitCode, IO, IOApp}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{EntityDecoder, EntityEncoder, HttpApp, HttpRoutes, Request, Response}

import scala.concurrent.ExecutionContext.{global => ec}

object Http4sApp extends IOApp {

  case class Student(age: Int, name: String, message: String)
  object Student {
    implicit val encoder: Encoder[Student] = deriveEncoder
    implicit val decoder: Decoder[Student] = deriveDecoder
    implicit val entityEncoder: EntityEncoder[IO, Student] = jsonEncoderOf
    implicit val entityDecoder: EntityDecoder[IO, Student] = jsonOf
  }

  /** core functions */
  def core(name: String) = s"Hello, $name."
  def core2(name: String) = Student(33, name, core(name))

  /** wire http */
  val http: PartialFunction[Request[IO], IO[Response[IO]]] = {
    /** http://localhost:8080/hello/Jim */
    case GET -> Root / "hello" / name => Ok(core(name))
    /** http://localhost:8080/hello2/Jackson */
    case GET -> Root / "hello2" / name => Ok(core2(name))
  }

  /** wire to the routes */
  val coreRoutes: HttpRoutes[IO] = HttpRoutes.of[IO](http)

  /** wire to the whole routes
    *{{{
    *  HttpApp[F[_]] = Http[F, F]
    *  Http[F[_], G[_]] = Kleisli[F, Request[G], Response[G]]
    *}}}
    */
  val allRoutes: HttpApp[IO] = Router(
    "/" -> coreRoutes
  ).orNotFound

  /** stream */
  val httpStream: fs2.Stream[IO, ExitCode] = BlazeServerBuilder[IO](ec)
    .bindHttp(8080, "localhost")
    .withHttpApp(allRoutes)
    .serve

  /** application */
  val app: IO[Unit] = httpStream
    .compile
    .drain

  /** entry point */
  override def run(args: List[String]): IO[ExitCode] = app.map(_ => ExitCode.Success)

}
