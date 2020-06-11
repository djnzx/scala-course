package fp_red.red09

object MyJSONParser {
  trait JSON
  object JSON {
    case class JNumber(get: Double) extends JSON
    case class JString(get: String) extends JSON
    case class JBool(get: Boolean) extends JSON
    case class JArray(get: IndexedSeq[JSON]) extends JSON
    case class JObject(get: Map[String, JSON]) extends JSON
  }

  def jsonParser[Parser[+_]](P: Parsers[Parser]): Parser[JSON] = {
    import P._
    val spaces: Parser[String] = char(' ').many.slice

    val intExtractor: Parser[Int] = for {
      digit <- "[0..9]+".r
      n = digit.toInt
      _ <- listOfN(n, char('a'))
    } yield n

    ???
  }

}
