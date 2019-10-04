package x015json

import play.api.libs.json.{JsValue, Json}

object JsonWrite03PlainObjectParsed extends App {

  val parsed1: JsValue = Json.parse("""
    {
      "name": "Alex",
      "age": 44
    }
    """)
  println(parsed1)

}
