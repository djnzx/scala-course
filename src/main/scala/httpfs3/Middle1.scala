package httpfs3

import cats.Functor
import cats.data.{Kleisli, OptionT}
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._

object Middle1 {

  def addRsHeader[F[_]: Functor](rs: Response[F], header: Header): Response[F] =
    rs match {
      // that header will be added to any successful response
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp => resp
    }

  def apply[F[_]: Functor](service: HttpRoutes[F], header: Header): HttpRoutes[F] =
    service.map(rs => addRsHeader(rs, header))

}
