package gas104.spec

import cats.effect.IO
import cats.implicits._
import gas104.Credentials
import gas104.Htttp._
import gas104.domain._
import gas104.domain.api._
import io.circe.parser
import io.circe.syntax.EncoderOps
import org.http4s.client.middleware.FollowRedirect
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Header, Method, Request}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.ci.CIStringSyntax

import java.time.{Instant, LocalDateTime}

class LoginExperimentsSpec extends AnyFunSpec with Matchers {

  import Credentials._

  describe("getting csrf token") {

    it("1") {
      import cats.effect.unsafe.implicits.global

      val x = obtainCsrfToken[IO]
        .unsafeRunSync()
      pprint.pprintln(x)
    }

  }

  describe("try to obtain session id") {
    it("1") {
      import cats.effect.unsafe.implicits.global

      //////////////// CSRF TOKEN ////////////////
      val (csrf, (ck, cv)) = obtainCsrfToken[IO].unsafeRunSync().get
      pprint.pprintln(csrf)
      pprint.pprintln(ck)
      pprint.pprintln(cv)

      val rq: Request[IO] = mkRequestLogin[IO](login104, password104, csrf, (ck, cv))

      //////////////// LOGIN ////////////////
      val (body0, hs, st) = mkHttpClient[IO]
        .flatMap(_.run(rq))
        .use { rs =>
          body(rs).map((_, rs.headers, rs.status))
        }
        .unsafeRunSync()

      val (ck2, cv2) = extractCookie(hs).get
      //        .foreach(h => pprint.pprintln(h))

      pprint.pprintln(st)
      pprint.pprintln(body0)

      val url = uri"https://account.104.ua/ua/account/index"

      val index = Request[IO](Method.GET, url)
        .withHeaders(Header.Raw(ci"$ck2", cv2))

      //////////////// /ua/account/index ////////////////
      val (b, s, hs2) = mkHttpClient[IO]
        .flatMap(_.run(index))
        .use { rs =>
          body(rs)
            .map(b =>
              (
                b,
                rs.status,
                rs.headers
              )
            )
        }.unsafeRunSync()

      pprint.pprintln(b)
      pprint.pprintln(s)
      hs2.foreach(x => pprint.pprintln(x))

      //////////////// /ua/login ////////////////
      val (ck3, cv3) = extractCookie(hs2).get
      val urlLogin = uri"https://account.104.ua/ua/login"
      val login = Request[IO](Method.GET, urlLogin)
        .withHeaders(Header.Raw(ci"$ck3", cv3))

      val (b2, s2, hs3) = mkHttpClient[IO]
        .flatMap(_.run(login))
        .use { rs =>
          body(rs)
            .map(b =>
              (
                b,
                rs.status,
                rs.headers
              )
            )
        }.unsafeRunSync()

      pprint.pprintln(b2)
      pprint.pprintln(s2)
      hs3.foreach(x => pprint.pprintln(x))

    }
    it("2") {
      import cats.effect.unsafe.implicits.global

      //////////////// CSRF TOKEN ////////////////
      val (csrf, (ck, cv)) = obtainCsrfToken[IO].unsafeRunSync().get
      pprint.pprintln(csrf)
      pprint.pprintln(ck)
      pprint.pprintln(cv)

      val rq: Request[IO] = mkRequestLogin[IO](login104, password104, csrf, (ck, cv))

      //////////////// LOGIN ////////////////
      val (body0, hs, st) = mkHttpClient[IO]
        .map(cl => FollowRedirect(5, _ => true)(cl))
        .flatMap(_.run(rq))
        .use { rs =>
          body(rs).map((_, rs.headers, rs.status))
        }
        .unsafeRunSync()

      val (ck2, cv2) = extractCookie(hs).get
      //        .foreach(h => pprint.pprintln(h))

      hs.headers.foreach(x => pprint.pprintln(x))
      pprint.pprintln(st)
      pprint.pprintln(body0)
      //
      //      val url = uri"https://account.104.ua/ua/account/index"
      //
      //      val index = Request[IO](Method.GET, url)
      //        .withHeaders(Header.Raw(ci"$ck2", cv2))
      //
      //      //////////////// /ua/account/index ////////////////
      //      val (b, s, hs2) = mkHttpClient[IO]
      //        .flatMap(_.run(index))
      //        .use { rs =>
      //          body(rs)
      //            .map(b =>
      //              (
      //                b,
      //                rs.status,
      //                rs.headers
      //              )
      //            )
      //        }.unsafeRunSync()
      //
      //      pprint.pprintln(b)
      //      pprint.pprintln(s)
      //      hs2.foreach(x => pprint.pprintln(x))
      //
      //      //////////////// /ua/login ////////////////
      //      val (ck3, cv3) = extractCookie(hs2).get
      //      val urlLogin = uri"https://account.104.ua/ua/login"
      //      val login = Request[IO](Method.GET, urlLogin)
      //        .withHeaders(Header.Raw(ci"$ck3", cv3))
      //
      //      val (b2, s2, hs3) = mkHttpClient[IO]
      //        .flatMap(_.run(login))
      //        .use { rs =>
      //          body(rs)
      //            .map(b =>
      //              (
      //                b,
      //                rs.status,
      //                rs.headers
      //              )
      //            )
      //        }.unsafeRunSync()
      //
      //      pprint.pprintln(b2)
      //      pprint.pprintln(s2)
      //      hs3.foreach(x => pprint.pprintln(x))
      //
    }
  }

}
