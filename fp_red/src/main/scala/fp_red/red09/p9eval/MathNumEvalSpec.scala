package fp_red.red09.p9eval

import fp_red.red09.p1impl.Reference
import fp_red.red09.p2concrete.math_num.MathOpToNumberParser
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MathNumEvalSpec extends AnyFunSpec with Matchers {

  describe("eval:math") {
    val R = Reference
    import MathNumEval.eval
    import MathOpToNumberParser.built

    it("1") {
      val raw = "(5-1)*3"
      R.run(built)(raw).map { eval } shouldBe Right(12)
    }

    it("2") {
      val raw = "(36-12)/(6+2)"
      R.run(built)(raw).map { eval } shouldBe Right(3)
    }
  }
  
}
