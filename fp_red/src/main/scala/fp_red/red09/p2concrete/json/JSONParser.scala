package fp_red.red09.p2concrete.json

import fp_red.red09.p0trait.Parsers

import scala.language.implicitConversions

object JSON {
  // domain model representation
  trait JSON
  case object JNull extends JSON
  case class JNumber(get: Double) extends JSON
  case class JString(get: String) extends JSON
  case class JBool(get: Boolean) extends JSON
  case class JArray(get: IndexedSeq[JSON]) extends JSON
  case class JObject(get: Map[String, JSON]) extends JSON

  // actual implementation
  def parser[Parser[+_]](P: Parsers[Parser]): Parser[JSON] = {
    // we'll hide the string implicit conversion and promote strings to tokens instead
    // this is a bit nicer than having to write token everywhere
    import P.{string => _, _}
    
    implicit def tok(s: String) = token(P.string(s))

    /** basic grammar */
    val jNull = "null".as(JNull)
    val jDouble = double.map(JNumber(_))
    val jString = escapedQuoted.map(JString(_))
    val bTrue = "true".as(JBool(true))
    val bFalse = "false".as(JBool(false))
    val jLiteral = scope("literal") { jNull | jDouble | jString | bTrue | bFalse }

    /** recursive grammar */
    def keyval = escapedQuoted ** (":" *> value)
    
    def value: Parser[JSON] = jLiteral | jObject | jArray

    def jArray = surround("[","]") {
      value sep "," map (vs => JArray(vs.toIndexedSeq))
    } scope "array"

    def jObject = surround("{","}") {
      keyval sep "," map (kvs => JObject(kvs.toMap))
    } scope "object"
    
    /** entry point */
    root(whitespace *> (jObject | jArray))
  }
}
