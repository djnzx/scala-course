package catsparse

import cats.implicits.catsSyntaxEitherId
import cats.parse.Rfc5234._
import cats.parse.{Parser => P}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** https://github.com/typelevel/cats-parse */
class BackTrackingSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {
  case class User(id: Int, name: String)
  val raw = Seq(
    "id:37 name:Alex",
    "id:37   name:Alex",
    "id:37   name:     Alex",
    "id:  37   name:     Alex",
    "  id:  37   name:     Alex",
    "id:  37   name:     Alex   ",
    "   id:  37   name:     Alex   ",
    " id:  37/name:     Alex       ",
    "  id:  37/ name:     Alex     ",
    "  id:  37  /name:     Alex    ",
    "  id:  37   / name:     Alex  ",
  )

  val slash     = P.char('/')
  val sp        = wsp | crlf // space / tab / cr /lfw
  val sp1       = sp.rep     // 1+
  val sp0       = sp.rep0    // 0+
  val delim     = (slash surroundedBy sp0).backtrack | sp1
  val idName    = P.string("id:")
  val nameName  = P.string("name:")
  val idValue   = P.charsWhile(_.isDigit).mapFilter(_.toIntOption)
  val nameValue = alpha.rep(1).string
  val asTuple   = (sp0 *> idName *> sp0 *> idValue) ~ delim ~ (nameName *> sp0 *> nameValue <* sp0)
  val asClass   = asTuple.map { case ((id, _), age) => User(id, age) }

  val expected = User(37, "Alex")
  val table    = Table(
    "raw" -> "expected",
    raw.map(_ -> expected): _*
  )

  def parse0(raw: String) = asClass
    .parse(raw)
    .map { case (_, u) => u }

  object User {
    def parse(raw: String): Either[P.Error, User] = parse0(raw)
  }

  test("1") {
    forAll(table) { case (in, out) =>
      User.parse(in) shouldBe out.asRight
    }
  }

}
