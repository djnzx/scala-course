package _sandbox

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object Sandbox {

  def id[A](a: A): A = a

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Sandbox._

  test("1") {
    id(1) shouldBe 1
  }

  test("exception syntax") {
    an[NumberFormatException] shouldBe thrownBy("q".toInt)
  }

  test("exception syntax with details") {
    1 shouldEqual 1
    val x: NumberFormatException = the[NumberFormatException] thrownBy {
      "q".toInt
    }
    x.getClass shouldBe classOf[NumberFormatException]
    x.getMessage shouldBe "For input string: \"q\""
  }

}
