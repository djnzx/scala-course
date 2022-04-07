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

  // Int => String
  // Kleisli[Id, Int, String]
  def add1(x: Int): String = x.toString

  // Int => Option[String]
  // Kleisli[Option, Int, String]
  // Kleisli[F, A, B] === A => F[B]
  def add2(x: Int): Option[String] = if (x > 10) Some(x.toString) else None

  val add3a: Kleisli[Option, Int, String] = Kleisli { x => if (x > 10) Some(x.toString) else None }
  val add3b = Kleisli { (x: Int) => if (x > 10) Some(x.toString) else None }
  def handler1[F[_]](rq: Request[F]): Option[F[Response[F]]] = ???
  def handler2[F[_]](rq: Request[F]): F[Option[Response[F]]] = ???
  def handler3[F[_]](rq: Request[F]): OptionT[F, Response[F]] = ???
  // any HTTP handler can be expressed in terms of Kleisli.
  def handler4[F[_]]: Kleisli[OptionT[F, *], Request[F], Response[F]] = ???

  /*
   * any MiddleWare:
   * Kleisli[OptionT[F, *], Request[F], Response[F]] => Kleisli[OptionT[F, *], Request[F], Response[F]]
   * HttpMiddleware[F] = Middleware[OptionT[F, *], Request[F], Response[F], Request[F], Response[F]]
   *
   * any HttpHandler is a f: Request[F] => Option[F[Response[F]]]
   * because not all Requests can be handled
   * in terms of OptionT: Request[F] => OptionT[F, Response[F]]
   * in terms of Kleisli: Kleisli[OptionT[F, *], Request[F], Response[F]]
   * so any Middleware:
   * Kleisli[OptionT[F, *], Request[F], Response[F]] => Kleisli[OptionT[F, *], Request[F], Response[F]]
   * Middleware[F[_], A, B, C, D] = Kleisli[F, A, B] => Kleisli[F, C, D]
   * HttpMiddleware[F[_]]    = Middleware[OptionT[F, *], Request[F],          Response[F], Request[F], Response[F]]
   * AuthMiddleware[F[_], T] = Middleware[OptionT[F, *], AuthedRequest[F, T], Response[F], Request[F], Response[F]]
   */

  def apply0[F[_]: Sync: Monad](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
    handler =>
      Kleisli { request: Request[F] =>
        val core: F[Option[Response[F]]] = handler.run(request).value
        val combined: F[Option[Response[F]]] =
          pre(request)
            .flatMap(_ => core)
            .flatTap(_.fold(().pure[F])(post(_)))
//            .flatTap(_.map(post(_)).getOrElse(().pure[F]))
        OptionT(combined)
      }

  def apply[F[_]: Sync: Monad](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
    handler =>
      Kleisli { request: Request[F] =>
//        OptionT(pre(request).map(_.some))
        OptionT
          .liftF(pre(request))
          //       A => OptionT[F, B]
          .flatMap(_ => handler.run(request))
          //          f: A => F[B]
          .semiflatTap(rs => post(rs))
      }

  def apply1[F[_]: Sync: Monad](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
    (originalHandler: Kleisli[OptionT[F, *], Request[F], Response[F]]) => // will be given later
      Kleisli { request: Request[F] =>
        val preF: F[Unit] = pre(request)
        val originalResponseF: F[Option[Response[F]]] = originalHandler(request).value
        val originalResponsePostF: F[Option[Response[F]]] =
          originalResponseF.flatTap(_.fold(().pure[F])(rs => post(rs)))
        val full: F[Option[Response[F]]] = preF *> originalResponsePostF
        OptionT(full)
      }

  def apply2[F[_]: Sync: Monad](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
    handler =>
      Kleisli { request: Request[F] =>
        // before lifted to OptionT[F, Unit]
        val beforeOptT: OptionT[F, Unit] = OptionT(pre(request).map(_.some))
        val originalResult: OptionT[F, Response[F]] = handler.run(request)
        val combinedWithPre: OptionT[F, Response[F]] = beforeOptT >> originalResult
        val combinedWithPreWithPost: OptionT[F, Response[F]] = combinedWithPre.semiflatTap(rs => post(rs))
        combinedWithPreWithPost
      }

  def logRequest[F[_]: Sync](rq: Request[F]): F[Unit] =
    Sync[F].delay(println(s"L: Before: $rq"))

  def logResponse[F[_]: Sync: Monad](rs: Response[F]): F[Unit] = for {
    _ <- F.delay(println(s"L: After: $rs"))
    body <- rs.body.through(text.utf8.decode).compile.string
    _ <- F.delay(println(s"Body: $body"))
  } yield ()

  def apply[F[_]: Sync: Monad]: HttpMiddleware[F] =
    apply(logRequest[F] _, logResponse[F] _)

}
