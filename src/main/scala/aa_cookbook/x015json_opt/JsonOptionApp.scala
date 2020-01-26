package aa_cookbook.x015json_opt

import play.api.libs.json.{JsValue, Json}

// different serialization implementations
//import OptPersonWriter1Bad._
//import OptPersonWriter2Good._
import OptPersonWriter3Default._

// different deserialization implementations
//import OptPersonReader1My1._
//import OptPersonReader1My2._
//import OptPersonReader1My3._
//import OptPersonReader1My4._
import OptPersonReader3Default._

// colored console
import Console._

object JsonOptionApp extends App {
  println(s"${BLUE}Json Deal with Option${RESET}")

  println(s"${GREEN}text(json) with field:${RESET}")
  println(stringSome)

  println(s"${GREEN}text(json) w/o field:${RESET}")
  println(stringNone)

  println(s"${GREEN}object with field:${RESET}")
  println(personSome)

  println(s"${GREEN}object w/o field:${RESET}")
  println(personNone)

  println(s"${YELLOW}encoding object with extra to json${RESET}")
  val encodedSome: JsValue = Json.toJson(personSome)
  println(Json.prettyPrint(encodedSome))

  println(s"${YELLOW}encoding object w/o extra to json${RESET}")
  val encodedNone: JsValue = Json.toJson(personNone)
  println(Json.prettyPrint(encodedNone))

  println(s"${YELLOW}decoding text with wield to object${RESET}")
  val decodedSome: OptPerson = Json.parse(stringSome).as[OptPerson]
  println(decodedSome)

  println(s"${YELLOW}decoding text w/o wield to object${RESET}")
  val decodedNone: OptPerson = Json.parse(stringNone).as[OptPerson]
  println(decodedNone)
}
