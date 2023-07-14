package vqpg

import io.circe.Encoder
import io.circe.Json
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import java.time.LocalDate

case class Certificate(
    id: String,
    channelId: String,
    achType: String,
    aType: String,
    number: Int,
    date: LocalDate,
    encoded: String,
    x: Option[Boolean] = None
)

object Certificate {

  implicit val certificateEncoder: Encoder[Certificate] = deriveEncoder[Certificate]
    .mapJsonObject {
      _.remove("achType")
        .add("bestMonth", Json.Null)
    }

}

object Test extends App {
  val c = new Certificate(
    "sdkfmgsd",
    "UC1234567890",
    "milestone",
    "views",
    5555,
    LocalDate.now(),
    "encoded value"
  )

  println(c.asJson)

}
