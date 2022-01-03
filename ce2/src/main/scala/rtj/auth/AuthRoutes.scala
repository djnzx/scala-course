package rtj.auth

import cats.Monad
import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import rtj.auth.AuthApi.AuthFailed
import rtj.auth.AuthApi.AuthRequest
import rtj.auth.AuthApi.AuthSuccess
import rtj.auth.AuthDomain.Password
import rtj.auth.AuthDomain.UserName

class AuthRoutes[F[_]: Monad: Sync](authService: AuthService) extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root => Ok("hello")
    case rq @ POST -> Root / "login" =>
      rq
        .attemptAs[AuthRequest]
        .leftMap(_ => BadRequest())
        .map { case AuthRequest(user, pass) =>
          authService.auth(UserName(user), Password(pass)) match {
            case Left(message) =>
              Forbidden(AuthFailed(message))
            case Right(token) =>
              Ok(AuthSuccess(token))
          }
        }
        .fold(identity, identity)
        .flatten
  }

}
