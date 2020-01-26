package aa_cookbook.x015json_opt

import play.api.libs.json.{Json, Writes}

object OptPersonWriter3Default {

  // default compile time implementation
  // thanks to Lightbend developers, correctly works with option
  implicit val defaultPersonWriter: Writes[OptPerson] = Json.writes[OptPerson]

}
