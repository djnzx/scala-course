package x015json_opt

import play.api.libs.json.{JsValue, Json, Writes}

object OptPersonWriter1Bad {

    // my brute force implementation to work with Option[] - core 9 lines, but with duplication
    implicit val personWriteBadDesignButWorking: Writes[OptPerson] = new Writes[OptPerson] {
      override def writes(p: OptPerson): JsValue = p.extra match {
        case Some(ex) => Json.obj(
          "name" -> p.name,
          "age" -> p.age,
          "extra" -> ex
        )
        case None     => Json.obj(
          "name" -> p.name,
          "age" -> p.age,
        )
      }
    }

}
