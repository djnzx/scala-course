package httpfs3

import java.nio.charset.StandardCharsets
import java.time.{LocalDate, LocalDateTime, Month, Year, ZoneId, ZoneOffset}

import cats.data.NonEmptyList
import cats.effect.{Clock, Sync}
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import org.http4s.CacheDirective.`no-cache`
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Cache-Control`
import org.http4s._
import org.http4s.dsl.impl.{FlagQueryParamMatcher, OptionalQueryParamDecoderMatcher, QueryParamDecoderMatcher, QueryParamMatcher, ValidatingQueryParamDecoderMatcher}

import scala.util.Try

/**
  * https://http4s.org/v0.21/dsl/
  */
object ServerRoutes {

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

  // custom path extractor
  object LocalDateVar {
    def unapply(str: String): Option[LocalDate] = Try(LocalDate.parse(str)).toOption
  }

  // response, based on path segments
  def routeB[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of {
      case GET -> Root / "b1" / name             => Ok(s"b1(string): $name")
      case GET -> Root / "b2" / IntVar(id)       => Ok(s"b2(int): $id")
      case GET -> Root / "b3" / LongVar(id)      => Ok(s"b3(long): $id")
      case GET -> Root / "b4" / UUIDVar(id)      => Ok(s"b4(uuid): $id")
      case GET -> Root / "b5" / LocalDateVar(da) => Ok(s"b5(date): $da")
    }
  }
  // custom parameter parsing
  object ParCountry extends QueryParamDecoderMatcher[String]("country")
  // custom parameter parsing with type converting
  implicit val yearDecoder: QueryParamDecoder[Year] = QueryParamDecoder[Int].map(Year.of)
  object ParYear extends QueryParamDecoderMatcher[Year]("year")
  // custom optional parameter parsing
  implicit val monthDecoder: QueryParamDecoder[Month] = QueryParamDecoder[Int].map(Month.of)
  object ParOptMonth extends OptionalQueryParamDecoderMatcher[Month]("month")
  // manual handling missed parameter
  object ParValidYear extends ValidatingQueryParamDecoderMatcher[Year]("year")
  object FlagX extends FlagQueryParamMatcher("x")

  // dsl syntax
  def routeC[F[_]: Sync: Clock]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      // just status 200
      case GET -> Root / "c1"   => Response[F](Status.Ok).pure[F]
      // status 200 and content
      case GET -> Root / "c2"   => Ok("Ok")
      // headers management
      case GET -> Root / "c3"   => Ok("Ok + cache", `Cache-Control`(NonEmptyList(`no-cache`(), Nil)))
      // cookies set
      case GET -> Root / "c4"   => for {
        resp <- Ok("Ok + set cookies")
        now = LocalDateTime.now(ZoneId.of("GMT")).plusMinutes(10)
        nowi = now.toInstant(ZoneOffset.ofHours(0))
        plus10m = HttpDate.unsafeFromInstant(nowi)
      } yield resp.addCookie(ResponseCookie("a", "b", expires = Some(plus10m), httpOnly = true))
      // get cookies from rq
      case rq @ GET -> Root / "c5"   => for {
        resp <- Ok(s"Ok. cookie from rq: ${rq.cookies.mkString}")
      } yield resp
      // cookies remove
      case GET -> Root / "c6"   => for {
        resp <- Ok("Ok + cookie remove")
      } yield resp.removeCookie("a")
      // binary content
      case GET -> Root / "c7"   => Ok("binary".getBytes(StandardCharsets.UTF_8))
      // chunks after "c8"
      case GET -> "c8" /: chunks => Ok(s"""c8+${chunks.segments.mkString("_", "_", "_")}!""")
      case GET -> "c9" /: "a" /: "b" /: ab=> Ok(s"/c/c9/a/b/ ${ab.segments.mkString("_", "_", "_")}")
      // '.json' extractor (http://127.0.0.1:8090/c/c10/aaa.json) => file=aaa
      case GET -> Root / "c10" / file ~ "json" => Ok(s"""{"response": "You asked for $file"}""")
      case GET -> Root / "c11" :? ParCountry(co) +& ParYear(year) +& ParOptMonth(m) => Ok(s"c11 GOT:${co}_${year}_$m")
      // year is presented but didn't pass the validation
      case GET -> Root / "c12" :? ParValidYear(yearValid) => yearValid.fold(
        failures => BadRequest("/c/c12: year should be specified"),
        year => Ok(s"/c/c12: year: $year")
      )
      case GET -> Root / "c13" :? FlagX(x) => Ok(s"c13 GOT:x=${x}") // true/false
      // root
      case GET -> Root  => Ok("root")
      // everything else
      case _   => Ok("/c/_")
    }
  }

  def routeD[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "200" => Ok("200")
      case GET -> Root / "201" => Created("201")
      case GET -> Root / "202" => Accepted("202")

      case GET -> Root / "400" => BadRequest("400")
      case GET -> Root / "403" => Forbidden("403")
      case GET -> Root / "404" => NotFound("204")
    }
  }

}
