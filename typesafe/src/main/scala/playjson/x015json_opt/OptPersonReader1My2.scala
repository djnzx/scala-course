package playjson.x015json_opt

import play.api.libs.json._

object OptPersonReader1My2 {

  // my implementation #2 shortened
  implicit val personReader: Reads[OptPerson] = (json: JsValue) => {
    JsSuccess[OptPerson](OptPerson(
      (json \ "name" get).as[String],
      (json \ "age" get).as[Int],
      (json \ "extra" toOption).map(j => j.as[String])
    ))
  }
}
