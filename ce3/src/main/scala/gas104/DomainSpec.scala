package gas104

import cats.effect.IO
import cats.implicits._
import gas104.Htttp._
import gas104.domain._
import gas104.domain.api._
import io.circe.parser
import io.circe.syntax.EncoderOps

import java.time.Instant
import java.time.LocalDateTime
import org.http4s.Header
import org.http4s.Method
import org.http4s.Request
import org.http4s.client.middleware.FollowRedirect
import org.http4s.implicits.http4sLiteralsSyntax
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.ci.CIStringSyntax

class DomainSpec extends AnyFunSpec with Matchers {

  import Credentials._

  describe("Row object") {

    val rowObj: Row = Row(
      "20.07.2023 07:00",
      "0.81",
      0,
      1689800400,
    )

    val rowRaw =
      """
        |{
        |  "dt" : "20.07.2023 07:00",
        |  "counter_reading" : "0.81",
        |  "consumption" : 0.0,
        |  "created_at_timestamp" : 1689800400
        |}
        |""".stripMargin.trim

    it("encodes properly") {
      rowObj.asJson.spaces2 shouldBe rowRaw
    }

    it("decodes properly") {
      parser.decode[Row](rowRaw) shouldBe rowObj.asRight
    }

  }

  describe("data") {

    val dataObj: Data = Data(
      error = None,
      data = Seq(
        Row(
          "20.07.2023 07:00",
          "0.81",
          0,
          1689800400,
        ),
        Row(
          "21.07.2023 07:00",
          "0.81",
          0,
          1689886800,
        )
      ).some
    )

    val rawData =
      """
        |{
        |  "error" : null,
        |  "data" : [
        |    {
        |      "dt" : "20.07.2023 07:00",
        |      "counter_reading" : "0.81",
        |      "consumption" : 0.0,
        |      "created_at_timestamp" : 1689800400
        |    },
        |    {
        |      "dt" : "21.07.2023 07:00",
        |      "counter_reading" : "0.81",
        |      "consumption" : 0.0,
        |      "created_at_timestamp" : 1689886800
        |    }
        |  ]
        |}
        |""".stripMargin.trim

    it("encodes properly") {
      dataObj.asJson.spaces2 shouldBe rawData
    }

    it("decodes properly") {
      parser.decode[Data](rawData) shouldBe dataObj.asRight
    }

  }

  describe("convertor") {

    it("apiRow -> URow") {
      val rowFrom104: Row = Row(
        "20.07.2023 07:00",
        "0.81",
        0,
        1689800400,
      )

      val uRowExpected = URow(
        LocalDateTime.of(2023, 7, 20, 7, 0),
        counter = 0.81,
        delta = 0.0,
        createdAt = Instant.parse("2023-07-19T21:00:00Z")
      )

      URow.from(rowFrom104) shouldBe uRowExpected
    }

  }

  describe("instant playground") {
    val ts: Long         = 1696539600L
    val instant: Instant = Instant.ofEpochSecond(ts) // 2023-10-05T21:00:00Z // "06.10.2023 07:00"

    it("3") {
      pprint.pprintln(instant)
    }

  }

  describe("read meters - token required") {

    it("1") {


      import cats.effect.unsafe.implicits.global

      mkHttpClient[IO]
        .use(obtainData[IO](sessionId104))
        .flatMap(representData[IO])
        .unsafeRunSync()
    }

  }

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
