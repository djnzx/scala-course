package fp_red.red09.p2concrete.math_num

import fp_red.red09.p1impl.Reference
import org.scalatest.matchers.should.Matchers
import org.scalatest.funspec.AnyFunSpec

// 1
class MathOpToNumberSpec extends AnyFunSpec with Matchers {
  val R = Reference
  
  // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
  describe("recursive calculator done!") {
    import MathOpToNumberParser.built
    
    it("123") {
      Seq(
        "4/-2/2",
        "3/-1+5",
        "-1+2",
        "1-2",
        "1-2*3",
        "(1-2)*-3",
        "((1-2)*(3+4))/5-1",
      ).foreach { s => pprint.log(R.run(built)(s)) }
    }
  }

}
