package httpfs1

import cats.data.Kleisli
import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, Timer}
import cats.syntax.semigroupk._
import fs2.Stream
import httpfs1.serv.{HelloWorld, Jokes}
import org.http4s.{HttpApp, HttpRoutes, Request, Response}
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext

/**
  * Routes,
  * Handler Chains (Http Filters, Loggers)
  * Services, and dependencies for services
  */
object AppServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val code: Stream[F, ExitCode] = for {
      // client which will be used for Http requests
      httpClient <- BlazeClientBuilder[F](ExecutionContext.global).stream // Stream[F, A]
      // services (real implementations)
      helloWorldAlg: HelloWorld[F] = HelloWorld.impl[F]        // HelloWorld[F] = HelloWorld[IO]
      jokeAlg:       Jokes[F]      = Jokes.impl[F](httpClient) // Jokes[F]      = Jokes[IO]
      // Can also be done via a Router if you want to extract
      // a segments not checked in the underlying routes.

      x: HttpRoutes[F] = AppRoutes.helloWorldRoutes[F](helloWorldAlg)
      y: HttpApp[F] = x.orNotFound
      rqHandler: HttpApp[F] = (
        AppRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        AppRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound // HttpApp[F] = Kleisli[F, A, Response[F]]

      // requestHandler wrapped into logger
      rqHandlerLogged: HttpApp[F] =
        Logger.httpApp(true, true)(rqHandler) // HttpApp[F]

      server: BlazeServerBuilder[F] = BlazeServerBuilder[F](ExecutionContext.global)
        .bindHttp(8080, "0.0.0.0")
//        .withHttpApp(rqHandler)
        .withHttpApp(rqHandlerLogged)

      exitCode <- server.serve
    } yield exitCode

    code
  }.drain // Stream[F, INothing]

}
