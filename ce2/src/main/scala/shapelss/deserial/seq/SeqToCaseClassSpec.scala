package shapelss.deserial.seq

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.Generic
import shapelss.deserial.seq.SeqToCaseClass.Reader

class SeqToCaseClassSpec extends AnyFunSpec with Matchers {

  describe("deserialization Seq[String] to a case class") {

    case class ExcelLine(a: String, b: String, c: Boolean, d: Option[Double])
    object ExcelLine {
      val ga = Generic[ExcelLine]
      implicit val reader = Reader.pickAndRead(ga)
    }

    it("should deserialize 1") {
      Reader.read[ExcelLine](Vector("a", "bb", "true", "3.14")) shouldEqual Right(
        ExcelLine("a", "bb", true, Some(value = 3.14)),
      )
    }

    it("should deserialize 2") {
      Reader.read[ExcelLine](Vector("a", "bb", "false", "", "bla-bla-bla")) shouldEqual Right(
        ExcelLine("a", "bb", false, None),
      )
    }
  }

}
