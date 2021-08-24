package _http4s.uri_ideas

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class RequestUriSyntaxSpec extends AnyFunSpec with Matchers {

  val baseRequest: Request[IO] = Request[IO](Method.GET, uri"byName".withQueryParam("name", "Jim"))

  it("base case") {
    baseRequest.uri.renderString shouldEqual "byName?name=Jim"
  }

  val parentUri = uri"customer/"

  it("verbose syntax - we need to use with API given") {
    val prependedRq: Request[IO] =
      baseRequest.withUri(parentUri.resolve(baseRequest.uri))

    prependedRq.uri.renderString shouldEqual "customer/byName?name=Jim"
  }

  it("clean syntax - we want to have") {
    import RequestUriSyntax.RequestUriAddParentSyntax

    val prependedRq: Request[IO] =
      baseRequest.uriPrepend(parentUri)

    prependedRq.uri.renderString shouldEqual "customer/byName?name=Jim"
  }

}
