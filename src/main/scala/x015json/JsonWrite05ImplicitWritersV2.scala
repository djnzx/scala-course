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
  // implicit writers Combinator Pattern
//  implicit val locationWrites2: Writes[Location] = (
//    (JsPath \ "lat").write[Double] and
//      (JsPath \ "long").write[Double]
//    )(unlift(Location.unapply))
//
//  implicit val residentWrites2: Writes[Resident] = (
//    (JsPath \ "name").write[String] and
//      (JsPath \ "age").write[Int] and
//      (JsPath \ "role").writeNullable[String]
//    )(unlift(Resident.unapply))
//
//  implicit val placeWrites2: Writes[Place] = (
//    (JsPath \ "name").write[String] and
//      (JsPath \ "location").write[Location] and
//      (JsPath \ "residents").write[Seq[Resident]]
//    )(unlift(Place.unapply))

  val place = Place(
    "Watership Down",
    Location(51.235685, -1.309197),
    Seq(
      Resident("Fiver", 4, None),
      Resident("Bigwig", 6, Some("Owsla"))
    )
  )
//  val json = Json.toJson(place)
//  println(json)
}
