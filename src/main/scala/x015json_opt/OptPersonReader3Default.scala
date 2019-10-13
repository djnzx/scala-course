package x015json_opt

import play.api.libs.json.{Json, Reads}

object OptPersonReader3Default {

  // default compile time implementation
  // thanks to Lightbend developers, correctly works with option
  implicit val personReader: Reads[OptPerson] = Json.reads[OptPerson]

}
