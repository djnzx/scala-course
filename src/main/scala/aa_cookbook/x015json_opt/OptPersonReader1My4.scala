package aa_cookbook.x015json_opt

import play.api.libs.json._

import scala.util.{Failure, Success, Try}

object OptPersonReader1My4 {

  // my implementation #4 shortened
  implicit val personReader: Reads[OptPerson] = (json: JsValue) => {
    // explicit error handle
    Try(OptPerson(
      (json \ "name" get).as[String],
      (json \ "age" get).as[Int],
      (json \ "extra" toOption).map(j => j.as[String])
    )) match {
      case Success(value) => JsSuccess[OptPerson](value)
      case Failure(exception) => JsSuccess[OptPerson](OptPerson("AZ", -1, None))
    }
  }
}
