package fp_red.red09.p2concrete.json

import fp_red.red09.p0trait.{Location, ParseError}
import fp_red.red09.p1impl.Reference
import fp_red.red09.p2concrete.json.JSON._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

// 6
class JSONParserSpec extends AnyFunSpec with Matchers {

  val P = Reference
  val parser: String => Either[ParseError, JSON] = P.run(JSON.parser(P))
  
  describe("JSON") {
    it("+1:array with integers") {
      val text = """[1,2,3]"""
      parser(text) shouldBe
        Right(JArray(Vector(JNumber(1.0), JNumber(2.0), JNumber(3.0))))
    }

    it("+2:object with one number field") {
      val text = """{ "a" : 123 }"""
      parser(text) shouldBe
        Right(JObject(Map("a" -> JNumber(123))))
    }
    
    it("+3:object with array of integers") {
      val text = """{ "a" : [1,2,3] }"""
      parser(text) shouldBe
        Right(JObject(Map("a" -> JArray(IndexedSeq(JNumber(1),JNumber(2),JNumber(3))))))
    }

    it("+4:complete object") {
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
      parser(text) shouldBe
        Right(
          JObject(Map("Shares outstanding" -> JNumber(8.38E9), "Price" -> JNumber(30.66), "Company name" -> JString("Microsoft Corporation"), "Related companies" -> JArray(Vector(JString("HPQ"), JString("IBM"), JString("YHOO"), JString("DELL"), JString("GOOG"))), "Ticker" -> JString("MSFT"), "Active" -> JBool(true)))
        )
    }
    
    it("-1:malformed") {
      val malformed = """
{
  "Company name" ; "Microsoft Corporation"
}
"""
      parser(malformed) shouldBe
        Left(ParseError(List(
          (Location(malformed, 1), "object"),
          (Location(malformed, 20), "':'")
        )))
    }
    
    it("-2:malformed") {
      val malformed = """[["HPQ", "IBM", "YHOO", "DELL" ++ "GOOG"]]"""
      parser(malformed) shouldBe
        Left(
          ParseError(
            List(
              (Location(malformed, 0), "array"),
              (Location(malformed, 1), "array"),
              (Location(malformed, 31), "']'")
            )
          )
        )
    }
  }
}
