package fp_red.red09

import scala.language.implicitConversions

object Impl001 {

  type Parser[+A] = String => Either[ParseError,A]

  trait Idea001 extends Parsers[Parser] {

    override implicit def string(s: String): Parser[String] =
    (input: String) =>
      if (input.startsWith(s)) Right(s)
      else Left(Location(input).toError("Expected: " + s))
    
  }

}
