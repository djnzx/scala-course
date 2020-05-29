package httpfs0

import cats.data.Kleisli
import cats.effect.{ContextShift, IO, Timer}
import cats.syntax.either._
import cats.syntax.semigroupk._
import org.http4s.{EntityBody, HttpApp, HttpRoutes, Request, Response}
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import fs2.Stream
import http_book.Book
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.concurrent.ExecutionContext.global
import org.http4s.circe.CirceEntityDecoder._

object HttpRsApp extends App {
  // routes mapping
  val routeA: HttpRoutes[IO] = HttpRoutes.of[IO] { rq: Request[IO] => rq match {
    case rqx @ GET -> Root / "hello" / name => {
      println(s"GET: $rqx")
      Ok.apply(s"Hello, $name.")
    }
    case rqx @ POST -> Root / "book" => {
      // extract body
      val body: String = rqx.bodyAsText.compile.string.unsafeRunSync()
      println(s"POST: $rqx")
      println(s"POST BODY: $body")
      Ok.apply(body)
    }
  }}

  def modify(b: Book): Book = Book(b.name.toUpperCase, b.author.toUpperCase)

  val routeB: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case rqx @ POST -> Root / "bookx1" => for {
      body <- rqx.bodyAsText.compile.string
      rs   <- decode[Book](body)
                .map { modify }
                .map(_.asJson.noSpaces)
                .fold(
                  _ => BadRequest(),
                  r => Ok(r)
                )
    } yield rs

    case rqx @ POST -> Root / "bookx2" => rqx.attemptAs[Book]
      .map { modify }
      .fold(
        _ => BadRequest(),
        b => Ok(b.asJson.noSpaces)
      )
      .flatMap(identity)

    case rqx @ POST -> Root / "bookx3" => for {
      // automatic decoding
      book  <- rqx.as[Book]
      book2 = modify(book)
      // automatic encoding
      rs    <- Ok(book2)
    } yield rs

  }

  val allRoutes: HttpRoutes[IO] = routeA <+> routeB

  // whole application must be full function
  val httpApp: HttpApp[IO] = Router(
    "/" -> allRoutes
  ).orNotFound

  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  val server: BlazeServerBuilder[IO] = BlazeServerBuilder[IO](global)
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)

  val io = server
    .serve
    .compile
    .drain

  io.unsafeRunSync()
}
