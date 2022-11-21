package json

import Utils.{readDataAsJson, readJsonConfig, wrongState}
import io.circe.syntax.EncoderOps
import io.circe.{Json, JsonObject}

object JsonMappingPlayground extends App {

  def remap(j: Json, config: MappingsConfig): Json = {
    val c = j.hcursor
    config.mappings
      .foldLeft(JsonObject.empty) { (jo, mm) =>
        jo.add(
          mm.to,
          c.downField(mm.from)
            .as[Json]
            .getOrElse(wrongState(s"field `${mm.from}` should exist!"))
        )
      }
      .asJson
  }

  val config1 = readJsonConfig("mapping-config-1.json")
  val config2 = readJsonConfig("mapping-config-2.json")
  val d1 = readDataAsJson("data1.json")
  val d2 = readDataAsJson("data2.json")
  val d1m = remap(d1, config1)
  val d2m = remap(d2, config2)

  println("== config1 ==")
  println(config1)
  println("== data1 before mapping ==")
  println(d1)
  println("== data1 after mapping ==")
  println(d1m)

//  println("== config2 ==")
//  println(config2)
//  println("== data2 before mapping ==")
//  println(d2)
//  println("== data2 after mapping ==")
//  println(d2m)

}
