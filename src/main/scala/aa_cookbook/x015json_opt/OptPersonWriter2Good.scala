package aa_cookbook.x015json_opt

import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue, Writes}

object OptPersonWriter2Good {

  // my clearer implementation to work with Option[] - core 7 lines, NO duplication
  implicit val personWrite: Writes[OptPerson] = new Writes[OptPerson] {
    override def writes(p: OptPerson): JsValue = {
      val js = scala.collection.mutable.ListBuffer.empty[(String, JsValue)]
      js ++= Seq(
        "name" -> JsString(p.name),
        "age" -> JsNumber(p.age)
      )
      p.extra.foreach(ex => js += ("extra" -> JsString(ex)))
      JsObject(js.toSeq)
    }
  }

}
