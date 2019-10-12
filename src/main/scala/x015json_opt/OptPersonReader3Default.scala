package x015json_opt

import play.api.libs.json.{Json, Reads}

object OptPersonReader3Default {

  // default reade
  implicit val personReader: Reads[OptPerson] = Json.reads[OptPerson]

}
