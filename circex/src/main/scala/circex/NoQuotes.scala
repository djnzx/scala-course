package circex

import io.circe.Decoder
import io.circe.Encoder
import io.circe.Error
import io.circe.Json
import io.circe.syntax.EncoderOps

/** no-quotes helper methods to use in parameter parsing / encoding */
object NoQuotes {

  val q = '"'

  // TODO: don't we reinvent the wheel here ???
  def enquote(s: String): String = s"$q$s$q"

  /** parse literal without quotes (by adding them) */
  def parseNoQuotes[A: Decoder](s: String): Either[Error, A] = io.circe.parser.decode[A](enquote(s))

  /** plain strip quotes, v1 */
  def stripQuotes(s: String) = s match {
    case s if s.nonEmpty && s.head == q && s.last == q => s.substring(1, s.length - 1)
    case s                                             => s
  }

  /** plain strip quotes, v2 */
  def stripQuotes2(s: String) = s match {
    case s""""$unqoted"""" => unqoted
    case s                 => s
  }

  /** serialize to literal without quotes (by removing them) */
  implicit class ObjectNoQuotesSyntax[A](a: A) {
    def asJsonNoQuotes(implicit ev: Encoder[A]) = a.asJson.noQuotes
  }

  implicit class JsonNoQuotesSyntax(json: Json) {
    def noQuotes = json.noSpaces.noQuotes
  }

  implicit class StringStripQuotesSyntax(s: String) {
    def noQuotes = stripQuotes(s)
  }

}
