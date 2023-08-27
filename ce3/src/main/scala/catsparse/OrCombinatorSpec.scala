package catsparse

import cats.implicits.catsSyntaxEitherId
import cats.parse.Rfc5234._
import cats.parse.{Parser => P}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** https://github.com/typelevel/cats-parse */
class OrCombinatorSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  val table = Table(
    "raw"   -> "expected",
    "1 3"   -> (1, 3),
    "1   3" -> (1, 3),
    "1/3"   -> (1, 3),
    "1/ 3"  -> (1, 3),
    "1 /3"  -> (1, 3),
    "1 / 3" -> (1, 3)
  )

  val slash = P.char('/')
  val space = wsp | crlf // space / tab / cr /lfw
  val sp1   = space.rep  // 1+
  val sp0   = space.rep0 // 0+
  val d     = char.map(_ - '0')

  val delim = (slash surroundedBy sp0).backtrack | sp1

  test("or") {
    val parser             = d ~ (delim *> d)
    def parse(raw: String) = parser.parse(raw).map(_._2)

    forAll(table) { case (in, out) =>
      parse(in) shouldBe out.asRight
    }
  }

}
