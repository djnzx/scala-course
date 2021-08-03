package _http4s

import cats.effect.Effect
import cats.implicits.{catsSyntaxApplicativeId, toFunctorOps}
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

class HttpServiceBinding[F[_]: Effect] {

  val service: ServiceImplementation[F] = new ServiceImplementation[F]
  val ok: Response[F] = Response[F]()

  /** partial function Rq => Rs */
  val httpBinding0: PartialFunction[Request[F], F[Response[F]]] = {
    case GET -> Root =>
      val r: Response[F] = Response[F]().withEntity("test")
      r.pure[F]
  }

  /** partial function Tq => Rs lifted to Rq => Option[Rs] */
  val httpBinding: HttpRoutes[F] = HttpRoutes.of[F] {
    /** http://localhost:8080/hello/Jim */
    case GET -> Root / "hello" / name => service.core(name).map { x => ok.withEntity(x) }
    /** http://localhost:8080/hello2/Jackson */
    case GET -> Root / "hello2" / name => service.core2(name).map { x => ok.withEntity(x) }
  }

}
