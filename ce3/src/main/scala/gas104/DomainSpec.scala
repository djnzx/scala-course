package gas104

import cats.effect.Concurrent
import cats.effect.IO
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.implicits._
import gas104.domain._
import gas104.domain.api._
import io.circe.parser
import io.circe.syntax.EncoderOps
import java.time.Instant
import java.time.LocalDateTime
import org.http4s.Response
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.scalatest.Succeeded
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class DomainSpec extends AnyFunSpec with Matchers {

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
      )
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

  describe("http") {

    def body[F[_] : Concurrent](rs: Response[F]): F[String] =
      rs.body
        .through(fs2.text.utf8.decode[F])
        .compile
        .foldMonoid

    def mkHttpClient[F[_]: Async]: Resource[F, Client[F]] =
      BlazeClientBuilder[F].resource

    def mkRequest[F[_]: Concurrent](client: Client[F]) = {
      import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
      client.expect[Data](Htttp.rq[F])
    }

    def representData[F[_]: Sync](payload: Data): F[Unit] = payload.data
      .traverse_ { x: Row =>
        val r    = URow.from(x)
        val line = (r.dateTime, r.counter, r.delta)
        Sync[F].delay(pprint.pprintln(line))
      }

    it("1") {
      import cats.effect.unsafe.implicits.global
      mkHttpClient[IO]
        .use(mkRequest[IO])
        .flatMap(representData[IO])
        .unsafeRunSync()
    }

    it("stub tun trigger compilation") {
      Succeeded
    }

  }

}
