package el_meter

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import io.scalaland.chimney.Transformer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.swing.text.DateFormatter
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
import retry.RetryDetails
import retry.RetryPolicy
import retry.implicits.retrySyntaxError
import retryideas.RetryApp.x
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

  def leftPad(x: Int, width: Int): String = s"%${width}d".formatted(x)
  def leftPad(x: Double, width: Int): String = leftPad(x.toInt, width)

  test("leftPad") {
    val x = leftPad(1.123, 6)
    pprint.log(x)
  }

  test("date formatted") {
    val ldt = LocalDateTime.parse("2024-08-25T13:52")
    val rep = ldt.format(DateTimeFormatter.ISO_DATE_TIME)
    pprint.log(rep)
  }

  test("pprint") {
    pprint
      .log("Hello")
  }

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

    // we can have Ref[Option[LocalDateLime]
    // to track electricity absence
    // and send email once electricity is restored

    // java.lang.IllegalStateException: supervisor already shutdown
    //	at get @ fs2.internal.Scope.openScope(Scope.scala:275)
    //	at get @ fs2.internal.Scope.openScope(Scope.scala:275)
    //	at unique @ fs2.Compiler$Target$ConcurrentTarget.unique(Compiler.scala:194)
    //	at deferred @ fs2.internal.InterruptContext$.$anonfun$apply$1(InterruptContext.scala:114)

    val policy: RetryPolicy[IO] = {
      import retry.RetryPolicies._
      // 6 retries starting from 1 gives us +1 +2 +4 +8 +16 +32 = 63 sec ~= 1 min
      val growing: RetryPolicy[IO] = limitRetries[IO](5) join exponentialBackoff[IO](1.second)
      // constant never terminating retry
      val constant: RetryPolicy[IO] = constantDelay[IO](10.seconds)

      growing followedBy constant
    }

    val onError = (t: Throwable, d: RetryDetails) =>
      IO(println(LocalDateTime.now -> "device inaccessible retrying..."))

    val getDataWithRetry = getData
      .retryingOnAllErrors(policy, onError)

    fs2.Stream
      .awakeEvery[IO](5000.millis)
      .evalMap(_ => getDataWithRetry)
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
