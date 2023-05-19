package _newage

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PlaygroundSpec extends AnyFunSpec with Matchers {

  it("1") {
    1 shouldEqual 1
    an[NumberFormatException] shouldBe thrownBy("q".toInt)
  }

}
