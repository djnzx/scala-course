package http4context

import cats.Monad
import cats.data.Kleisli
import cats.data.OptionT
import cats.implicits._
import http4middle.accurate.ContextExtractor
import org.http4s.AuthedRequest
import org.http4s.AuthedRoutes
import org.http4s.HttpRoutes
import org.http4s.Request
import org.http4s.Response
import org.http4s.server.AuthMiddleware

object AuthMiddlewareAccurate {

  def of[F[_]: Monad, A, E](
      contextExtractorF: Request[F] => F[Either[E, A]],
      onContextFailureExtractionF: (E, Request[F]) => F[Response[F]],
      isHandlerDefined: Request[F] => Boolean
    ): AuthMiddleware[F, A] = {
    val keepUnhandled = none[Response[F]].pure[F]

    (routes: AuthedRoutes[A, F]) =>
      Kleisli { rq: Request[F] =>
        OptionT {
          contextExtractorF(rq).flatMap {
            case Right(context)                  => routes(AuthedRequest(context, rq)).value
            case Left(e) if isHandlerDefined(rq) => onContextFailureExtractionF(e, rq).map(_.some)
            case Left(_)                         => keepUnhandled
          }
        }
      }
  }

}

object AuthedRoutesAccurate {

  /** we need to provide a stubContext
    * to properly distinguish 401 / unhandled
    * it's used only for isDefinedAt
    */
  def of[F[_]: Monad, A, E](
      partialHandlerF: PartialFunction[AuthedRequest[F, A], F[Response[F]]],
      contextExtractorF: ContextExtractor.Extractor[F, E, A],
      stubContext: A, // actually any value of type A which passes pattern matching
      onContextFailureExtractionF: (E, Request[F]) => F[Response[F]]
    ): HttpRoutes[F] = {

    /** when context extraction is failed we need to check whether the handler is defined
      * with the stub context to properly distinguish 401 / unhandled
      */
    def isPartialHandlerDefined(rq: Request[F]): Boolean = partialHandlerF.isDefinedAt(AuthedRequest(stubContext, rq))

    val middleware: AuthMiddleware[F, A] = AuthMiddlewareAccurate.of(
      contextExtractorF,
      onContextFailureExtractionF,
      isPartialHandlerDefined
    )

    val authedRoutes = AuthedRoutes.of(partialHandlerF)
    middleware(authedRoutes)
  }

  /** less parameters to return 401 Unauthorized by default
    * enough for 99% cases
    */
  def of[F[_]: Monad, A, E](
      partialHandlerF: PartialFunction[AuthedRequest[F, A], F[Response[F]]],
      contextExtractorF: ContextExtractor.Extractor[F, E, A],
      stubContext: A
    ): HttpRoutes[F] = of(
    partialHandlerF,
    contextExtractorF,
    stubContext,
    (_: E, rq: Request[F]) => AuthMiddleware.defaultAuthFailure[F].apply(rq)
  )
}
