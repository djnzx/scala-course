package _http4s

import cats.effect.IO
import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId, none}
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{DecodeFailure, EntityDecoder, HttpRoutes, Method, Request}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Http4sTesting extends AnyFunSpec with Matchers {

  def runTest[A](routes: HttpRoutes[IO])(rq: Request[IO])(result: Option[Either[DecodeFailure, A]])(implicit d: EntityDecoder[IO, A]) =
    routes(rq)             // PartialFunction[Request[IO], IO[Response[IO]]]
      .value
      .unsafeRunSync()     // run handler
      .map { rs =>
        rs
          .attemptAs[A]    // EitherT[IO, Err, String]
          .value           // F[Either[Err, String]]
          .unsafeRunSync() // run decoder
      } shouldEqual result

  describe("1") {

    val serviceRoutes: HttpRoutes[IO] = new HttpServiceBinding[IO].httpBinding

    it("GET with PathVariable") {
      val uri = uri"hello" / "Jim"
      val rq: Request[IO] = Request(Method.GET, uri)
      val exp = "Hello, Jim."

      runTest(serviceRoutes)(rq)(exp.asRight.some)
    }

    it("GET with QueryParam") {
      val rq: Request[IO] = Twice.request[IO]
      val exp = "6"

      runTest(serviceRoutes)(rq)(exp.asRight.some)
    }

    /** TODO: */
    it("POST with entity in Request and entity in Response") {
    }

    /** TODO */
    it("bad request (Match Error)") {
    }

    it("definitely unhandled case") {
      val uri = uri"unhandled"
      val rq: Request[IO] = Request(Method.GET, uri)

      runTest[String](serviceRoutes)(rq)(none)
    }

  }
}
