package x015json

import play.api.libs.json.{JsObject, Json, Writes}

case class Person(name: String, age: Int)

object JsonWrite06ImplicitWritersMy extends App {
  implicit val personWriter: Writes[Person] = (p: Person) => Json.obj("name" -> p.name, "age" -> p.age)

  val p = Person("alex", 42)
  val json = Json.toJson(p)

  println(json.getClass)
  println(json)
}
