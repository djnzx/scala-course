package clipboard

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object Playground extends AnyFunSuite with Matchers with App {

  test("1") {
    Clipboard.set("whatever")
    Clipboard.get shouldBe Some("whatever")
  }

  test("2") {
    val x = Literature.remap("5,8,19,30")
    pprint.log(x)
  }

}
