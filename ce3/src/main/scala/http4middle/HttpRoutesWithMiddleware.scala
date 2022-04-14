package http4middle

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.Request
import org.http4s.Response

object HttpRoutesWithMiddleware {

  type Handler[F[_]] = PartialFunction[Request[F], F[Response[F]]]
  type MiddleWare[F[_]] = Handler[F] => Handler[F]

  private def wrapIfDefined[F[_]: Monad](middleware: MiddleWare[F])(handler: Handler[F]): Handler[F] = {
    case req if handler.isDefinedAt(req) => middleware(handler)(req)
  }

  def of[F[_]: Monad](middleware: MiddleWare[F])(handler: Handler[F]): HttpRoutes[F] =
    HttpRoutes.of(
      wrapIfDefined(middleware)(handler),
    )

}
