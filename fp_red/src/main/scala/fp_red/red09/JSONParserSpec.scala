package fp_red.red09

import fp_red.red09.JSON.{JArray, JBool, JNumber, JObject, JString}
import org.scalatest.{EitherValues, Inside, Inspectors, OptionValues, funspec, matchers}

class JSONParserSpec extends funspec.AnyFunSpec
  with matchers.should.Matchers
  with OptionValues
  with EitherValues
  with Inside
  with Inspectors {

  val P: Reference.type = Reference
  val json = JSON.jsonParser(P)
  val parser: String => Either[ParseError, JSON] = P.run(json)
  
  describe("number") {

    val jsonTxt = """
{
  "Company name" : "Microsoft Corporation",
  "Ticker"  : "MSFT",
  "Active"  : true,
  "Price"   : 30.66,
  "Shares outstanding" : 8.38e9,
  "Related companies" : [ "HPQ", "IBM", "YHOO", "DELL", "GOOG" ]
}
"""

    val r = parser(jsonTxt)
    r.right.value should beJObject(Map("Shares outstanding" -> JNumber(8.38E9), "Price" -> JNumber(30.66), "Company name" -> JString("Microsoft Corporation"), "Related companies" -> JArray(Vector(JString("HPQ"), JString("IBM"), JString("YHOO"), JString("DELL"), JString("GOOG"))), "Ticker" -> JString("MSFT"), "Active" -> JBool(true)))
  }
}
