package el_meter

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import org.http4s.Header
import org.http4s.MediaType
import org.http4s.Method
import org.http4s.Request
import org.http4s.Status.Successful
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sLiteralsSyntax
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.typelevel.ci.CIStringSyntax

object Http {

  private def mkHttpClient =
    BlazeClientBuilder[IO].resource

  private val url = uri"http://192.168.7.38?page=getdata&devid=1828726629&devpass=6543"
  import org.http4s.headers._
  val rq = Request[IO](Method.GET, url)
    .withHeaders(
      `Accept`(MediaType.application.json),
      Header.Raw(ci"User-Agent", "IntelliJ HTTP Client/IntelliJ IDEA 2024.2"),
      Header.Raw(ci"Accept-Encoding", "br, deflate, gzip, x-gzip"),
//      Header.Raw(ci"Accept", "*/*"),
      Header.Raw(ci"content-length", "0"),
    )

  import model._

  def getData = mkHttpClient
    .flatMap(_.run(rq))
    .use {
//      case x => x.body.compile.count.flatMap(x => IO(pprint.log(x))) >>
      case Successful(rs) => rs.attemptAs[RawResponse].value
        .flatMap {
          case Right(x) => x.pure[IO]
          case Left(x) => IO(pprint.log(x))
        }
      case x => IO(pprint.log(x)) >> IO.raiseError(new RuntimeException("wrong response"))
    }

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Http._

  test("1") {
    getData
      .flatMap(x => IO(pprint.log(x)))
      .unsafeRunSync()
  }

}
