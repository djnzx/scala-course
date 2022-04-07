package http4middle

import cats.Monad
import cats.data.Kleisli
import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import fs2.text
import org.http4s.Request
import org.http4s.Response
import org.http4s.server.HttpMiddleware

object LoggerMiddleware {

  /*
   * any HttpHandler is a f: Request[F] => Option[F[Response[F]]]
   * because not all Requests can be handled
   * in terms of OptionT: Request[F] => OptionT[F, Response[F]]
   * in terms of Kleisli: Kleisli[OptionT[F, *], Request[F], Response[F]]
   *
   * so any Middleware:
   *
   * Kleisli[OptionT[F, *], Request[F], Response[F]] => Kleisli[OptionT[F, *], Request[F], Response[F]]
   *
   * Middleware[F[_], A, B, C, D] = Kleisli[F, A, B] => Kleisli[F, C, D]
   * HttpMiddleware[F[_]]    = Middleware[OptionT[F, *], Request[F],          Response[F], Request[F], Response[F]]
   * AuthMiddleware[F[_], T] = Middleware[OptionT[F, *], AuthedRequest[F, T], Response[F], Request[F], Response[F]]
   */

//  def apply[F[_]: Sync: Monad: Logger]: HttpMiddleware[F] =
//    handler =>
//      Kleisli { request: Request[F] =>
//        val loggerBefore: F[Unit] = F.delay(println("L:Logged before"))
//        val loggerAfter: F[Unit] = F.delay(println("L:Logged After"))
////        val loggerBefore: F[Unit] = Logger[F].info("Logged before")
////        val loggerAfter: F[Unit] = Logger[F].info("Logged After")
//
//        /** approach 1, opening OptionT */
////        val x: F[Option[Response[F]]] = handler.run(request).value
////        val combined: F[Option[Response[F]]] = loggerBefore *> x <* loggerAfter
////        OptionT(combined)
//
//        /** approach 2, combination */
//        OptionT(loggerBefore.map(_.some))
//          .flatMap(_ => handler.run(request))
//          .semiflatTap(_ => loggerAfter)
//      }

  def apply[F[_]: Sync: Monad](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
    handler =>
      Kleisli { request: Request[F] =>
        OptionT(pre(request).map(_.some))
          .flatMap(_ => handler.run(request))
          .semiflatTap(rs => post(rs))
      }

  def logRequest[F[_]: Sync](rq: Request[F]): F[Unit] = F.delay(println(s"L: Before: $rq"))

  def logResponse[F[_]: Sync: Monad](rs: Response[F]): F[Unit] = for {
    _ <- F.delay(println(s"L: After: $rs"))
    body <- rs.body.through(text.utf8.decode).compile.string
    _ <- F.delay(println(s"Body: $body"))
  } yield ()

  def apply[F[_]: Sync: Monad]: HttpMiddleware[F] = apply(logRequest[F] _, logResponse[F] _)

}
