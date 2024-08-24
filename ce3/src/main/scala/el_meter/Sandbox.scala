package el_meter

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import io.scalaland.chimney.Transformer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.http4s.Method
import org.http4s.Request
import org.http4s.Status.BadRequest
import org.http4s.Status.ClientError
import org.http4s.Status.Successful
import org.http4s.Status.TooManyRequests
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sLiteralsSyntax
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.concurrent.duration.DurationInt

object Http {

  def mkHttpClient =
    BlazeClientBuilder[IO].resource

  private val url = uri"http://192.168.7.38?page=getdata&devid=1828726629&devpass=6543"
  val rq = Request[IO](Method.GET, url)

  import model._

  def getData = mkHttpClient
    .flatMap(_.run(rq))
    .use {
      case Successful(rs)                                  => rs.as[RawResponse].map(_.some)
      case ClientError(rs) if rs.status == TooManyRequests => IO(None)
      case x                                               => IO(pprint.log(x)) >> IO.raiseError(new RuntimeException("wrong response"))
    }

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Http._
  import io.scalaland.chimney.inlined._
  import model._
//  import io.scalaland.chimney.dsl._

  test("round") {
    pprint.log(round1(1.23456))
  }

  test("instant") {
    val raw = "1724506617"
    val ldt = LocalDateTime.ofInstant(
      Instant.ofEpochSecond(raw.toInt),
      ZoneOffset.ofHours(3)
    )
    pprint.log(ldt)

  }

  test("one value") {
    getData
      .map(_.getOrElse(???))
      .map(_.into[DataLine].transform)
      .map(_.into[DataLineWattOnlyDetailed].transform)
      .flatMap(x => IO(pprint.log(x)))
      .unsafeRunSync()
  }

  test("streamed") {
    fs2.Stream
      .awakeEvery[IO](5000.millis)
      .evalMap(_ => getData)
      .unNone
      .map(_.into[DataLine].transform)
      .map(_.into[DataLineWattOnlyDetailed].transform)
      .map(_.into[DataLineWattOnlyShort].transform)
      .evalTap(x => IO(pprint.log(x)))
      .compile
      .drain
      .unsafeRunSync()
  }

}
