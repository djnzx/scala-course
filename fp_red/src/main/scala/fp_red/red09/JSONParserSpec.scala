package fp_red.red09

import fp_red.red09.JSON._
import org.scalatest.{Inside, Inspectors, funspec, matchers}

class JSONParserSpec extends funspec.AnyFunSpec
  with matchers.should.Matchers
  with Inside
  with Inspectors {

  val P: Reference.type = Reference
  val json = JSON.jsonParser(P)
  val parser: String => Either[ParseError, JSON] = P.run(json)
  
  describe("JSON") {
    it("object with one number field") {
      val text = """{ "a" : 123 }"""

      parser(text) shouldEqual Right(JObject(Map("a" -> JNumber(123))))
    }

    it("array of integers") {
      val text = """{ "a" : [1,2,3] }"""

      parser(text) shouldEqual Right(JObject(Map("a" -> JArray(IndexedSeq(JNumber(1),JNumber(2),JNumber(3))))))
    }

    it("complete object") {

      val text = """
{
  "Company name" : "Microsoft Corporation",
  "Ticker"  : "MSFT",
  "Active"  : true,
  "Price"   : 30.66,
  "Shares outstanding" : 8.38e9,
  "Related companies" : [ "HPQ", "IBM", "YHOO", "DELL", "GOOG" ]
}
"""

      parser(text) shouldEqual
        Right(
          JObject(Map("Shares outstanding" -> JNumber(8.38E9), "Price" -> JNumber(30.66), "Company name" -> JString("Microsoft Corporation"), "Related companies" -> JArray(Vector(JString("HPQ"), JString("IBM"), JString("YHOO"), JString("DELL"), JString("GOOG"))), "Ticker" -> JString("MSFT"), "Active" -> JBool(true)))
        )
    }

  }
  
}
