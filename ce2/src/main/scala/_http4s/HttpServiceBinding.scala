package _http4s

import cats.implicits.{catsSyntaxApplicativeId, toFunctorOps}
import cats.{Applicative, Defer, Functor}
import io.circe.syntax.EncoderOps
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits.http4sLiteralsSyntax

/**
  * these names make sense only in terms of this service.
  * we shouldn't to expose them to kind of common
  * and we event shouldn't be able to use them outside
  */
object names {
  val x = "x"
}

/**
  * these ParamMatchers names make sense only in terms of this service.
  * we shouldn't to expose them to kind of common
  */
object ParamX extends QueryParamDecoderMatcher[Int](names.x)

object Twice {
  /** request parser to use in HttpRoutes */
  def unapply[F[_]](rq: Request[F]): Option[Int] = rq match {
    case GET -> Root / "twice" :? ParamX(x) => Some(x)
    case _ => None
  }
  /** request constructor to make requests */
  def request[F[_]]: Request[F] = Request[F](
    Method.GET,
    uri"twice".withQueryParam(names.x, 3)
  )
}

class HttpServiceBinding[F[_]: Applicative: Functor: Defer] {

  val service: ServiceImplementation[F] = new ServiceImplementation[F]
  def ok[A](a: A)(implicit ea: EntityEncoder[F, A]): Response[F] = Response[F](status = Ok).withEntity(a)

  /** partial function Rq => Rs */
  val httpBinding0: PartialFunction[Request[F], F[Response[F]]] = {
    case GET -> Root =>
      val r: Response[F] = Response[F]().withEntity("test")
      r.pure[F]
  }

  private val asciiStringWithoutQuotesEncoder = new EntityEncoder[F, String] {
    override def toEntity(a: String): Entity[F] = Entity(fs2.Stream.emits(a.map(_.toByte)).covary)
    override def headers: Headers = Headers.empty
  }
  /** partial function Tq => Rs lifted to Rq => Option[Rs] */
  val httpBinding: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "q"  => Response[F](Status.Ok).withEntity("quotes").pure[F]
    /** http://localhost:8080/hello/Jim */
    case GET -> Root / "hello" / name => service.core(name).map { x => ok(x) }
    /** http://localhost:8080/hello2/Jackson */
    case GET -> Root / "hello2" / name => service.core2(name).map { x => ok(x) }
    /** direct combination, but implies code duplication */
//    case GET -> Root / "twice" :? ParamX(x) => service.twice(x).map { x => ok.withEntity(x.toString) }
    /** via our unapply */
    case Twice(x) => service.twice(x).map { x => ok(x.toString) }
  }

}
