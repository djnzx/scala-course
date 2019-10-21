package x015json_custom2

import play.api.libs.json.{JsString, JsValue, Json, OWrites, Writes}

sealed trait MsgFlag
case object Important extends MsgFlag
case object Offensive extends MsgFlag
case object Spam extends MsgFlag
case object Normal extends MsgFlag

final case class Content(src: Long, dst: Long, text: String, flag: Option[MsgFlag] = None, id: Long = 0L)

object Custom2App extends App {
  implicit val fs: MsgFlag => String = {
    case Important => "!"
    case Offensive => "$"
    case Spam =>      "X"
    case Normal =>    " "
  }
  implicit val fw: Writes[MsgFlag] = (o: MsgFlag) => JsString(o)
  implicit val cw: OWrites[Content] = Json.writes[Content]
  val ct1 = Content(123, 234, "brilliant", Some(Important))
  println(Json.prettyPrint(Json.toJson(ct1)))
}
