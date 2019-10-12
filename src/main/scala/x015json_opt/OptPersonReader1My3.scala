package x015json_opt

import play.api.libs.json._

object OptPersonReader1My3 {

  // my implementation #3 shortened
  implicit val personReader: Reads[OptPerson] = (json: JsValue) => {
    JsSuccess[OptPerson](OptPerson(
      (json \ "name").as[String],
      (json \ "age").as[Int],
      (json \ "extra").asOpt[String]
    ))
  }
}
