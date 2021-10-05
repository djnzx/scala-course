package _circe

import com.google.cloud.logging.Payload.JsonPayload
import io.circe.{Json, JsonNumber, JsonObject}
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.{lang, util}
import scala.jdk.CollectionConverters.SeqHasAsJava

class ConversionSpec extends AnyFunSpec with Matchers {

  /** JSON Boolean handling */
  def mapBoolean(b: Boolean): lang.Boolean = new java.lang.Boolean(b)

  /** JSON Number handling */
  def mapJsonNumber(n: JsonNumber): Object =
    n.toLong
      .getOrElse(n.toDouble)
      .asInstanceOf[Object]

  /** JSON String handling */
  def mapString(s: String): String = new java.lang.String(s)

  /** JSON Array handling */
  def mapJsonArray(xs: Vector[Json]): util.List[Object] =
    xs.map { json => foldJsonToObject(json) } .asJava

  def foldJsonToObject(json: Json): Object = json.fold(
    null,
    mapBoolean,
    mapJsonNumber,
    mapString,
    mapJsonArray,
    mapJsonObject
  )

  /** JSON Object handling */
  def mapJsonObject(src: JsonObject): util.Map[String, Object] = {
    val m = new util.HashMap[String, Object]
    src.toMap.foreach { case (field, json) =>
      val value = foldJsonToObject(json)
      m.put(field, value)
    }
    m
  }

  it("0") {
    val cMap = Map(
      "a" -> 1.asJson,
      "b" -> 3.14.asJson,
      "c" -> "jim".asJson,
      "d" -> Vector(10,11,12).asJson,
      "z" -> None.asJson,
    ).asJsonObject
    println(cMap)

    val gMap = mapJsonObject(cMap)
    println(gMap)
    println(JsonPayload.of(gMap))
  }

  it("1") {
    val m = new util.HashMap[String, Object]()
    m.put("a", new java.lang.Integer(1))
    m.put("b", new java.lang.Long(2))
    m.put("c", new java.lang.Double(3))
    m.put("d", new lang.Float(4))
    m.put("e", new java.lang.String("jim"))
    m.put("f", new java.lang.Boolean(true))
    m.put("x", null)
    val jpl = JsonPayload.of(m)
    println(jpl)
  }

}
