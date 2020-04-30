package httpfs2

import cats.effect.Sync
import cats.syntax.flatMap._
import httpfs2.serv.ServiceA
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityEncoder, HttpRoutes}

object AppRoutes {

  // just response on path, without parsing
  def routeA[F[_]: Sync](sa: ServiceA[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    implicit val entEncI: EntityEncoder[F, Int]           = jsonEncoderOf  // 1
    implicit val entEncLI: EntityEncoder[F, List[Int]]    = jsonEncoderOf  // 2
    implicit val entEncLS: EntityEncoder[F, List[String]] = jsonEncoderOf  // 3

    HttpRoutes.of[F] {
      case GET -> Root / "a1" => Ok(1)                       // 1
      case GET -> Root / "a2" => Ok("2")                     // by default
      case GET -> Root / "a3" => Ok(List(1,2,3))             // 2
      case GET -> Root / "a4" => Ok(List("a","b"))           // 3
      case GET -> Root / "a5" => sa.get. flatMap(Ok(_))       // handled by ServiceA.ResponseA1
      case GET -> Root / "a6" => sa.get2.flatMap(Ok(_))       // 3
    }
  }

  // response, based on path segments
  def routeB[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of {
      //                        extracting Int from path
      case GET -> Root / "b1" / name        => Ok(s"b1(string): $name")
      case GET -> Root / "b2" / IntVar(id)  => Ok(s"b2(int): $id")
      case GET -> Root / "b3" / LongVar(id) => Ok(s"b3(long): $id")
    }
  }

  def routeC[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "c" => Ok("c")
    }
  }

}
