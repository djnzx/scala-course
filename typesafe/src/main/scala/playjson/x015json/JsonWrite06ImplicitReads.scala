package playjson.x015json

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

object JsonWrite06ImplicitReads extends App {
  implicit val personWriter: Writes[Person1] = (p: Person1) => Json.obj("name" -> p.name, "age" -> p.age)

  // default mapping
//  implicit val personReader0: Reads[Person1] = Json.reads[Person1]

  // full custom implementation, we can make a lot of custom checks
//  implicit val personReader1: Reads[Person1] = new Reads[Person1] {
//    override def reads(json: JsValue): JsResult[Person1] = {
//      // read fields
//      val nlr: JsLookupResult = json \ "name"
//      val alr: JsLookupResult = json \ "age"
//      // extract option
//      val njv: JsValue = nlr.get // JsString
//      val ajv: JsValue = alr.get // JsNumber
//      // cast to types
//      val n: String = njv.as[String]
//      val a: Int = ajv.as[Int]
//      JsSuccess[Person1](Person1(n, a))
//    }
//  }

  // custom mapping extended syntax way
//  implicit val personReader2: Reads[Person1] = (
//      (JsPath \ "name").read[String] and
//      (JsPath \ "age").read[Int]
//    ) (Person1.apply _)
  implicit val personReader3: Reads[Person1] = (
      (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Int]
    ) (Person1.apply _)

  val p = Person1("alex", 42)
  val json = Json.toJson(p)

  println(json.getClass)
  println(json)

  val p2 = json.as[Person1]
//  val p2 = json.as[Person1](Json.reads[Person1])
  println(p2.name)
  println(p2.age)
  val t1 = (1, "Alex")
}
