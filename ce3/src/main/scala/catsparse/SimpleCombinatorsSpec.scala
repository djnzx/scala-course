package catsparse

import cats.implicits._
import cats.parse.{Parser, Parser0}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** https://github.com/typelevel/cats-parse */
class SimpleCombinatorsSpec extends AnyFunSuite with Matchers {

  case class Box(c: Char)

  val raw1 = "5bla-bla-bla"
  val raw2 = "5"
  val raw3 = ""

  test("1") {
    /** this parser takes the first char */
    val parser: Parser[Box] = Parser.anyChar.map(Box)

    parser.parse(raw1) shouldBe ("bla-bla-bla", Box('5')).asRight
  }

  test("2") {
    /** this parser will fail if string is not empty */
    val parser: Parser[Box] = Parser.anyChar.map(Box) <* Parser.end

    parser.parse(raw1).isLeft shouldBe true
    parser.parse(raw2) shouldBe ("", Box('5')).asRight
  }

  test("3") {
    /** this parser can handle errors via Option[A] */
    val parser: Parser0[Option[Box]] = Parser.anyChar.map(Box).?

    parser.parse(raw1) shouldBe ("bla-bla-bla", Box('5').some).asRight
    parser.parse(raw2) shouldBe ("",            Box('5').some).asRight
    parser.parse(raw3) shouldBe ("",            none         ).asRight
  }

}
