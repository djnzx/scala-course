package _http4s

import org.http4s.Query
import org.http4s.Uri
import org.http4s.Uri.Authority
import org.http4s.Uri.Ipv4Address
import org.http4s.Uri.Scheme.http
import org.http4s.implicits.http4sLiteralsSyntax
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class UriExploreSpec extends AnyFunSpec with Matchers {

  describe("expect exactly what being fed") {
    it("1") {
      val p0: Uri = Uri.unsafeFromString("part0")
      p0.renderString shouldEqual "part0"
    }
    it("2") {
      val p0: Uri = Uri.unsafeFromString("part0/")
      p0.renderString shouldEqual "part0/"
    }
    it("3") {
      val p0: Uri = Uri.unsafeFromString("/part0")
      p0.renderString shouldEqual "/part0"
    }
    it("4") {
      val p0: Uri = Uri.unsafeFromString("/part0/")
      p0.renderString shouldEqual "/part0/"
    }
  }

  describe("uri syntax") {

    it("1") {
      uri"a".renderString shouldEqual "a"
      uri"a/".renderString shouldEqual "a/"
      uri"/a/".renderString shouldEqual "/a/"
      (uri"a" / "b").renderString shouldEqual "a/b"
    }

  }

  describe("prepend") {

    it("if there is no slash - just take child") {
      val parent: Uri = Uri.unsafeFromString("parent")
      val child: Uri = uri"child"

      parent.resolve(child).renderString shouldEqual "child"
    }

    it("if child starts from `/` we take child, no matter about parent") {
      val parent1: Uri = Uri.unsafeFromString("parent")
      val parent2: Uri = Uri.unsafeFromString("parent/")
      val parent3: Uri = Uri.unsafeFromString("/parent")
      val parent4: Uri = Uri.unsafeFromString("/parent/")
      val child: Uri = uri"/child"

      parent1.resolve(child) shouldEqual child
      parent2.resolve(child) shouldEqual child
      parent3.resolve(child) shouldEqual child
      parent4.resolve(child) shouldEqual child
    }

    it("parent with / - combine") {
      val parent: Uri = Uri.unsafeFromString("parent/")
      val child: Uri = uri"child"

      parent.resolve(child).renderString shouldEqual "parent/child"
    }

  }

  describe("combination") {
    it("1") {
      val uri = (uri"http://1.2.3.4:8081/a/" / "b" / "c")
        .withQueryParam("x", "33")
        .withQueryParam("y", "44")
        .withFragment("row=10")
//      val uri = uri"http://1.2.3.4:8081/a/b/c?x=33&y=44#row=10"

      pprint.pprintln(uri.renderString)
      pprint.pprintln(uri)

      uri shouldEqual Uri(
        scheme = Some(http),
        authority = Some(
          value = Authority(
            userInfo = None,
            host = Ipv4Address(a = 1, b = 2, c = 3, d = 4),
            port = Some(value = 8081),
          ),
        ),
        path = "/a/b/c",
        query = Query(
          ("x", Some("33")),
          ("y", Some("44")),
        ),
        fragment = Some("row=10"),
      )
    }
  }

  it("2") {
    val p1: Uri = uri"part1"
    val p2: Uri = p1.addPath("a")
    val p3: Uri = p1 / "b"
//    val p4: Uri = p0.resolve(p1)
//
//    println(p0)
    println(p1)
    println(p2)
    println(p3)
//    println(p4)
  }

}
