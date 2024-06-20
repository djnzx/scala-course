package catsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Rfc5234
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** https://github.com/typelevel/cats-parse */
class RecursiveSchemasSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  def ps[A](pa: Parser[A]): Parser[A] = pa.between(Parser.char('('), Parser.char(')'))

  sealed trait A
  case class A1(id: String)       extends A
  case class A2(id: String, a: A) extends A

  val id: Parser[String] = Rfc5234.alpha.rep.string

  def a1p: Parser[A1] = id.map(A1)
  def a2p: Parser[A2] = Parser.defer(id ~ ps(ap)).map(A2.tupled)
  def ap: Parser[A] = a2p.backtrack | a1p

  test("A") {

    val testData = Table(
      "input"   -> "output",
      "a"       -> A1("a"),
      "a(b)"    -> A2("a", A1("b")),
      "a(b(c))" -> A2("a", A2("b", A1("c"))),
    )

    forAll(testData) { (in, exp) =>
      val real = ap.parseAll(in)
      real shouldBe exp.asRight
    }
  }
}
