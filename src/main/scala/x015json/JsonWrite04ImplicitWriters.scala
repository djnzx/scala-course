package x015json

import play.api.libs.json._
//  JsValue
//  JsNumber, JsString, JsBoolean
//  JsObject, JsArray, JsNull
case class Location(lat: Double, long: Double)
case class Resident(name: String, age: Int, role: Option[String])
case class Place(name: String, location: Location, residents: Seq[Resident])

// taken from https://www.playframework.com/documentation/2.7.x/ScalaJson
object JsonWrite04ImplicitWriters extends App {
  // implicit writers
  implicit val locationWrites = new Writes[Location] {
    def writes(location: Location) = Json.obj(
      "lat" -> location.lat,
      "long" -> location.long
    )
  }
  implicit val residentWrites = new Writes[Resident] {
    def writes(resident: Resident) = Json.obj(
      "name" -> resident.name,
      "age" -> resident.age,
      "role" -> resident.role
    )
  }
  implicit val placeWrites = new Writes[Place] {
    def writes(place: Place) = Json.obj(
      "name" -> place.name,
      "location" -> place.location,
      "residents" -> place.residents
    )
  }

  val place = Place(
    "Watership Down",
    Location(51.235685, -1.309197),
    Seq(
      Resident("Fiver", 4, None),
      Resident("Bigwig", 6, Some("Owsla"))
    )
  )
  val json: JsValue = Json.toJson(place)
  println(json)

  // accessing by path without object conversion
  val lat: JsValue = (json \ "location" \ "lat").get // JsNumber(51.235685)
  val bigwig: JsValue = (json \ "residents" \ 1).get // {"name":"Bigwig","age":6,"role":"Owsla"}
  // Applying the \\ operator will do a lookup for the field in the current object and ALL descendants.
  val names = json \\ "name" // Seq(JsString("Watership Down"), JsString("Fiver"), JsString("Bigwig"))
  val name = json("name") // JsString("Watership Down")
  val bigwig2 = json("residents")(1) // {"name":"Bigwig","age":6,"role":"Owsla"}

  val minified: String = Json.stringify(json)
  val readable: String = Json.prettyPrint(json)

  println(minified)
  println(readable)
}
