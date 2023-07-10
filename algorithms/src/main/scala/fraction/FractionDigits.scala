package fraction

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object FractionDigits {

  def div(a: Int, b: Int, nDigitsAfterComma: Int): String = {

    def before(a: Int, b: Int) = ((a / b).toString, a % b)

    def after(a: Int, b: Int, buf: List[Char], n: Int): String = n match {
      case 0 => buf.reverse.mkString
      case _ =>
        val int = a / b
        val rem = a % b
        after(rem * 10, b, (int + '0').toChar :: buf, n - 1)
    }

    val (prefix, a2) = before(a, b)

    after(a2 * 10, b, (prefix + ".").reverse.toList, nDigitsAfterComma)
  }

  def findMissingGroups =
    div(1, 998001, 3000)
      .drop(2)
      .grouped(3)
      .toList
      .map(_.toInt)
      .sliding(2)
      .collect { case List(a, b) if b - a > 1 => (a, b, b - a) }
      .toList

}

class FractionDigitsSpec extends AnyFunSpec with Matchers {

  import FractionDigits._

  describe("number of digits after comma") {

    it("case 1") {
      div(1, 735, 50) shouldBe "0.00136054421768707482993197278911564625850340136054"
    }

    it("case 2") {
      div(1, 999*999, 12) shouldBe "0.000 001 002 003".filterNot(_ == ' ')
    }

    it("missing groups") {
      findMissingGroups shouldBe List((997, 999, 2))
    }

  }
}
