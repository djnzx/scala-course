package httpfs

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext

import serv.{HelloWorld, Jokes}

/**
  * Routes,
  * Handler Chains (Http Filters, Loggers)
  * Services, and dependencies for services
  */
object AppServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      // client which will be used for Http requests
      httpClient <- BlazeClientBuilder[F](ExecutionContext.global).stream
      // services (real implementations)
      helloWorldAlg = HelloWorld.impl[F]  // HelloWorld[F] = HelloWorld[IO]
      jokeAlg = Jokes.impl[F](httpClient) // Jokes[F]      = Jokes[IO]

      // Can also be done via a Router if you want to extract
      // a segments not checked in the underlying routes.
      rqHandler = (
        AppRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        AppRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound // Kleisli[F, A, Response[F]]

      // requestHandler wrapped into logger
      rqHandlerLogged = Logger.httpApp(true, true)(rqHandler)

      exitCode <- BlazeServerBuilder[F](ExecutionContext.global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(rqHandlerLogged)
        .serve
    } yield exitCode
  }.drain

}
