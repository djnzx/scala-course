package x015json

import play.api.libs.json.{JsValue, Json}

object JsonWrite03PlainObjectParsed extends App {

  // Object Value creating - by parsing JSON text
  val j6: JsValue = Json.parse("""
    {
      "name": "Alex",
      "age": 44
    }
    """)
  println(j6)

}
