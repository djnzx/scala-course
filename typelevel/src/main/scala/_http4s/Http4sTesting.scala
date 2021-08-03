package _http4s

import cats.Id
import cats.effect.IO
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Method, Request}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Http4sTesting extends AnyFunSpec with Matchers {

  describe("1") {
    it("1.1") {

      val rq: Request[IO] = Request(Method.GET, uri"/hello/Jim")
      val service = new HttpServiceBinding[IO]

      val rs = service.httpBinding(rq) // PartialFunction[Request[IO], IO[Response[IO]]]
        .value
        .unsafeRunSync()   // run handler
        .map(_
          .attemptAs[String] // EitherT[IO, Err, String]
          .value             // F[Either[Err, String]]
          .unsafeRunSync()   // run decoder
        )

      rs shouldEqual Some(Right("Hello, Jim."))

    }
  }
}
