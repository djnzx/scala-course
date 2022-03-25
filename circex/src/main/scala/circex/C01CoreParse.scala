package circex

import io.circe._
import io.circe.parser._

object C01CoreParse extends App {

  val raw: String = """
{
  "foo": "bar",
  "baz": 123,
  "list of stuff": [ 4, 5, 6 ]
}
"""
  val r1: Either[ParsingFailure, Json] = parse(raw)
  val r2: Json = r1.getOrElse(Json.Null)
  val cur = r2.hcursor
  cur.downField("foo").as[String].foreach(println) // bar
  cur.downField("zxc").as[Option[String]].foreach(println) // None

}
