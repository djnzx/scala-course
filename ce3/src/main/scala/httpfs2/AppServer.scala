package httpfs2

import cats.Applicative
import cats.effect._
import cats.syntax.semigroupk._
import fs2.Stream
import httpfs2.serv.ServiceA
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router

/**
  * Routes,
  * Handler Chains (Http Filters, Loggers)
  * Services, and dependencies for services
  */
object AppServer {

  def stream[F[_]: Async: Applicative]: Stream[F, Nothing] = {
    val sa: ServiceA[F] = ServiceA.impl[F]

    val myRoutes =
      AppRoutes.routeA[F](sa) <+>
      AppRoutes.routeB[F]
    val httpApp1: HttpApp[F] = myRoutes.orNotFound
    val httpApp2: HttpApp[F] = Router(
      "/a" -> AppRoutes.routeA(sa),
      "/b" -> AppRoutes.routeB,
      "/c" -> AppRoutes.routeC
    ).orNotFound

    val sb: BlazeServerBuilder[F] = BlazeServerBuilder[F]
      .bindHttp(8000, "localhost")
      .withHttpApp(httpApp2)

    sb.serve.drain
  }

}
