package x015json_opt

import play.api.libs.json.{JsLookupResult, JsResult, JsSuccess, JsValue, Reads}

object OptPersonReader1My1 {

  // my implementation #1
  implicit val personReader: Reads[OptPerson] = new Reads[OptPerson] {
    override def reads(json: JsValue): JsResult[OptPerson] = {
      // extract fields
      val nlr: JsLookupResult = json \ "name"
      val alr: JsLookupResult = json \ "age"
      val xlr: JsLookupResult = json \ "extra"
      // extract values
      val njv: JsValue = nlr.get // JsString
      val ajv: JsValue = alr.get // JsNumber

//      val xjv: JsValue = xlr.getOrElse(JsNull) // JsString
//      val extra = xjv match {
//        case JsNull => None
//        case JsString(x) => Some(x)
//      }

      val xjv: Option[JsValue] = xlr.toOption // JsString
      val extra = xjv.map(j => j.as[String])

      // cast to types
      val name: String = njv.as[String]
      val age: Int = ajv.as[Int]

      JsSuccess[OptPerson](OptPerson(name, age, extra))
    }
  }
}
