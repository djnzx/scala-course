package gas104

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.AutoDerivation
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.scalaland.chimney.dsl._

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField

object domain {

  object api {

    case class Row(
        dt: String,
        counterReading: String,
        consumption: Double,
        createdAtTimestamp: Long)

    object Row {
      implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
      implicit val encoder: Encoder[Row] = deriveConfiguredEncoder
      implicit val decoder: Decoder[Row] = deriveConfiguredDecoder
    }

    case class Data(error: Option[String], data: Option[Seq[Row]])

    object Data extends AutoDerivation

  }

  val gasDtFormatter: DateTimeFormatter = new DateTimeFormatterBuilder()
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .appendLiteral('.')
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .appendLiteral('.')
    .appendValue(ChronoField.YEAR, 4)
    .appendLiteral(' ')
    .appendValue(ChronoField.HOUR_OF_DAY, 2)
    .appendLiteral(':')
    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
    .toFormatter

  case class URow(dateTime: LocalDateTime, counter: Double, delta: Double, createdAt: Instant)
  object URow {

    def from(apiRow: api.Row): URow =
      apiRow
        .into[URow]
        .withFieldComputed(_.dateTime, x => LocalDateTime.parse(x.dt, gasDtFormatter))
        .withFieldComputed(_.counter, x => x.counterReading.toDouble)
        .withFieldRenamed(_.consumption, _.delta)
        .withFieldComputed(_.createdAt, x => Instant.ofEpochSecond(x.createdAtTimestamp))
        .transform

  }

}
