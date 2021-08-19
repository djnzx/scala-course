package org.alexr.lcx

import com.google.cloud.logging.Payload
import com.google.cloud.logging.Payload.JsonPayload
import com.google.protobuf.Struct

import java.util.{Map => JMap}
import scala.jdk.CollectionConverters.MapHasAsJava

object MyJsonPayload {

  case class JeepLogEntry(car: String, engine: String)

  trait Structable[A] {
    def toStruct(a: A): java.util.Map[String, String]
  }

  val encoder: Structable[JeepLogEntry] = new Structable[JeepLogEntry] {
    override def toStruct(a: JeepLogEntry): JMap[String, String] = Map(
      "car" -> a.car,
      "engine" -> a.engine,
    ).asJava
  }

  val jeepEntry: JeepLogEntry = JeepLogEntry("Grand Cherokee", "SRT6.4")
  val encoded: JMap[String, String] = encoder.toStruct(jeepEntry)

  val payload: Payload[Struct] = JsonPayload.of(encoded)

}
