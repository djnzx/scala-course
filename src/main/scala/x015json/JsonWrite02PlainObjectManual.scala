package x015json

import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue, Json}

object JsonWrite02PlainObjectManual extends App {

  // Object Value creating - manual explicit way #1
  val j51: JsValue = JsObject(Seq(
    "name" -> JsString("Alexey"),
    "age" -> JsNumber(33)
  ))

  // Object Value creating - manual explicit way #2
  val j52: JsValue = Json.obj(
    "name" -> JsString("Alexey"),
    "age" -> JsNumber(33)
  )

  println(j51)
  println(j51.getClass) // JsObject

  println(j52)
  println(j52.getClass) // JsObject

  println(j52.toString())
  println(j52.toString().getClass) // String

  val s53: String = Json.stringify(j52)
  println(s53)
  println(s53.getClass) // String

  println(Json.prettyPrint(j52))
}
