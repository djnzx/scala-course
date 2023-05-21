package http4middle.accurate

import cats.Monad
import cats.data.Kleisli
import cats.data.OptionT
import org.http4s.Response

object KleisliResponseOrElseSyntax {

  implicit class KleisliResponseOps[F[_]: Monad, A](self: Kleisli[OptionT[F, *], A, Response[F]]) {

    def orElse(elseHandler: A => F[Response[F]]): Kleisli[F, A, Response[F]] =
      Kleisli(rq => self.run(rq).getOrElseF(elseHandler(rq)))

  }

}
