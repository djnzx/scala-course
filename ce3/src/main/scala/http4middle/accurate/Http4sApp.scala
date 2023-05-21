package http4middle.accurate

import cats.data.Kleisli
import cats.data.OptionT
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Sync
import cats.implicits.toSemigroupKOps
import com.comcast.ip4s.IpLiteralSyntax
import http4context.AuthedRoutesAccurate
import http4middle.accurate.KleisliResponseOrElseSyntax.KleisliResponseOps
import org.http4s.AuthedRequest
import org.http4s.HttpRoutes
import org.http4s.Request
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.ci.CIStringSyntax

/** the only idea of this investigation is the fact
  * of impossibility to compose many authed routes
  * or handle different authorizations
  * or handle orElse clause after authorized routes
  *
  * as a result of this investigation
  * we understood we did premature lift to Rq => OptT[F, Response]
  * and we can't distinguish 401 fof really non-matched clauses
  */
object Http4sApp extends IOApp.Simple {

  class Services[F[_]: Sync] extends Http4sDsl[F] {

    /** try to handle the request */
    type PartialHandler          = PartialFunction[Request[F], F[Response[F]]]
    type PartialHandlerAuthed[A] = PartialFunction[AuthedRequest[F, A], F[Response[F]]]

    /** try to extract the context */
    type ContextExtractor[A] = Request[F] => Option[A]
    type Token               = String
    type SecretCode          = Int

    val tokenExtractor: ContextExtractor[Token]     = _.headers.get(ci"X-Token").map(_.head.value)
    val codeExtractor: ContextExtractor[SecretCode] = _.headers.get(ci"X-Code").flatMap(_.head.value.toIntOption)

    val open1: PartialHandler = { case rq @ GET -> Root / "a" => Ok(s"/a ${tokenExtractor(rq)}") }
    val open2: PartialHandler = { case rq @ GET -> Root / "b" => Ok(s"/b ${codeExtractor(rq)}") }

    /** composing of open is EASY */
    val open: PartialHandler      = open1 orElse open2
    val routesOpen: HttpRoutes[F] = HttpRoutes.of[F](open)

    /** just partial handler for authed, not lifted yet */
    val authed1: PartialHandlerAuthed[Token]      = { case GET -> Root / "c" as token => Ok(s"/c $token") }
    val authed2: PartialHandlerAuthed[SecretCode] = { case GET -> Root / "d" as code => Ok(s"/d $code") }

    val stubToken: Token     = "STUB!"
    val stubCode: SecretCode = Int.MinValue

    val authedMethod1: HttpRoutes[F] = AuthedRoutesAccurate.of(authed1, ContextExtractor.option(tokenExtractor), stubToken)
    val authedMethod2: HttpRoutes[F] = AuthedRoutesAccurate.of(authed2, ContextExtractor.option(codeExtractor), stubCode)

    /** just composition */
    val routesPartial: Kleisli[OptionT[F, *], Request[F], Response[F]] = routesOpen <+> authedMethod1 <+> authedMethod2

    /** this is my custom orElse handler */
    val customElseHandler = (rq: Request[F]) => Ok(s"orElse(${rq.uri})")

    /** full, but the problem here is: I can't write orElse */
    val routesFull = routesPartial orElse customElseHandler
  }

  override def run: IO[Unit] =
    EmberServerBuilder
      .default[IO]
      .withPort(port"8080")
      .withHttpApp(new Services[IO].routesFull)
//      .withHttpApp(new Services[IO].routesPartial.orNotFound)
      .build
      .use(_ => IO.never)
}
