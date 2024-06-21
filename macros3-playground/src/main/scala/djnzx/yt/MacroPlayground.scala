package djnzx.yt

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MacroPlayground extends AnyFunSuite with Matchers {

  test("enrich string with identifier name") {
    val x: String = "Jim"
    val y: String = MacroTool.debug(x)
    println(y)
    y shouldBe "identifier given: x = Jim"
  }

  test("enrich string in other way") {
    val x: String = MacroTool.debug("Jim")
    println(x)
    x shouldBe "literal given: Jim"
  }

}
