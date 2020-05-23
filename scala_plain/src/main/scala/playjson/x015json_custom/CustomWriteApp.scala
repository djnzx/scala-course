package playjson.x015json_custom

import play.api.libs.json.{JsString, Json, Writes}

sealed trait Flag
case object Important extends Flag
case object Spam extends Flag

case class Tweet(text: String, rate: Option[Int], flag: Flag, oflag: Option[Flag])

object CustomWriteApp extends App {

  implicit val f_s2: Flag => String = {
    case Important => "!"
    case Spam => "X"
  }
  implicit val fw: Writes[Flag] = (f: Flag) => JsString(f)
  implicit val tw = Json.writes[Tweet]

  val t1 = Tweet("I'm so smart", None, Important, None)
  val t2 = Tweet("I'm super smart", Some(42), Spam, Some(Important))
  println(Json.prettyPrint(Json.toJson(t1)))
  println(Json.prettyPrint(Json.toJson(t2)))
}
