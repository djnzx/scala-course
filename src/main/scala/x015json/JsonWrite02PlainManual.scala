package x015json

import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue, Json}

object JsonWrite02PlainManual extends App {

  val parsed2: JsValue = JsObject(Seq("name" -> JsString("Alexey"), "age" -> JsNumber(33)))
  println(parsed2)

}
