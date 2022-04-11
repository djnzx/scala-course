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

  def apply0[F[_]: Sync](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
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

  def apply[F[_]: Sync](
      pre: Request[F] => F[Unit],
      postHandled: Response[F] => F[Unit],
      postNotHandled: => F[Unit],
    ): HttpMiddleware[F] =
    (handler: Kleisli[OptionT[F, *], Request[F], Response[F]]) =>
      Kleisli { request: Request[F] =>
        val x = for {
          _ <- pre(request)
          rsOpt <- handler(request).value
          _ <- rsOpt.fold(postNotHandled)(postHandled)
        } yield rsOpt

        OptionT(x)
      }

  def apply4[F[_]: Sync](
      pre: Request[F] => F[Unit],
      postHandled: Response[F] => F[Unit],
      postNotHandled: => F[Unit],
    ): HttpMiddleware[F] =
    handler =>
      Kleisli { request: Request[F] =>
        val preF: F[Unit] = pre(request)
        val originalResponseF: F[Option[Response[F]]] = handler(request).value
        val originalResponsePostF: F[Option[Response[F]]] =
          originalResponseF.flatTap(_.fold(postNotHandled)(rs => postHandled(rs)))
        val full: F[Option[Response[F]]] = preF *> originalResponsePostF
        OptionT(full)
      }

  def apply3[F[_]: Sync](pre: Request[F] => F[Unit], postHandled: Response[F] => F[Unit]): HttpMiddleware[F] =
    handler =>
      Kleisli { request: Request[F] =>
//        OptionT(pre(request).map(_.some))
        OptionT
          .liftF(pre(request))
          //       A => OptionT[F, B]
          .flatMap(_ => handler.run(request))
          //          f: A => F[B]
          .semiflatTap(rs => postHandled(rs))
      }

  def apply1[F[_]: Sync](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
    (originalHandler: Kleisli[OptionT[F, *], Request[F], Response[F]]) => // will be given later
      Kleisli { request: Request[F] =>
        val preF: F[Unit] = pre(request)
        val originalResponseF: F[Option[Response[F]]] = originalHandler(request).value
        val originalResponsePostF: F[Option[Response[F]]] =
          originalResponseF.flatTap(_.fold(().pure[F])(rs => post(rs)))
        val full: F[Option[Response[F]]] = preF *> originalResponsePostF
        OptionT(full)
      }

  def apply2[F[_]: Sync](pre: Request[F] => F[Unit], post: Response[F] => F[Unit]): HttpMiddleware[F] =
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

  def logResponseHandled[F[_]: Sync](rs: Response[F]): F[Unit] = for {
    _ <- F.delay(println(s"L: After: $rs"))
    body <- rs.body.through(text.utf8.decode).compile.string
    _ <- F.delay(println(s"Body: $body"))
  } yield ()

  def logRequestNotHandled[F[_]: Sync]: F[Unit] =
    Sync[F].delay(println(s"L: After, not handled:"))

  def apply[F[_]: Sync: Monad]: HttpMiddleware[F] =
    apply(logRequest[F], logResponseHandled[F], logRequestNotHandled[F])

}
