package hr_parse.db.tables

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import io.getquill.MappedEncoding

object DateEndoders {
  implicit val encZdt: MappedEncoding[ZonedDateTime, Date] =
    MappedEncoding { z => Date.from(z.toInstant) }
  implicit val decZdt: MappedEncoding[Date, ZonedDateTime] =
    MappedEncoding { d => d.toInstant.atZone(ZoneOffset.UTC) }

}
