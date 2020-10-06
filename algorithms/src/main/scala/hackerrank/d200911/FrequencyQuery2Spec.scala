package hackerrank.d200911

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import pprint.{pprintln => println}

class FrequencyQuery2Spec extends AnyFunSpec with Matchers {
  
  describe("freq") {
    import FrequencyQuery2._
    
    describe("fold") {
      val cmds: Array[Cmd] = Array(
        Inc(5),
        Inc(7),
        Inc(5),
        Inc(10),
        Inc(10),
        Dec(10)
      )
      it("a") {
        println(fold(cmds))
      }
    }
  }

}
