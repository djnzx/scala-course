package aa_cookbook.x015json

import play.api.libs.json._

object JsonWrite01PlainValues extends App {

  // Values creating
  val j1: JsValue = Json.toJson(5)          // int, actually play.api.libs.json.JsNumber
  val j2: JsValue = Json.toJson("Hello")    // str
  val j3: JsValue = Json.toJson(true)       // bool
  val j4: JsValue = Json.toJson(Seq(1,2,3)) // array

  println(j1.getClass)
  println(j1)
  println(j4)

}
