package shapelessx.deserial.seq

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.Generic
import shapelessx.deserial.seq.SeqToCaseClass.Reader

class SeqToCaseClassSpec extends AnyFunSpec with Matchers {

  describe("deserialization Seq[String] to a case class") {

    case class ExcelLine(a: String, b: String, c: Boolean, d: Option[Double])
    object ExcelLine {
      implicit val reader = Reader.pickAndRead(Generic[ExcelLine])
    }

    it("should deserialize") {

      Reader.read[ExcelLine](Seq("a","bb","true", "3.14"))
        .fold(_ => ???, identity) shouldEqual
        ExcelLine("a", "bb", true, Some(value = 3.14))

      Reader.read[ExcelLine](Seq("a","bb","false", ""))
        .fold(_ => ???, identity) shouldEqual
        ExcelLine("a", "bb", false, None)
      
    }
  }
  
}
