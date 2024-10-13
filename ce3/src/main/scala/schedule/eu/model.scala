package schedule.eu

import io.circe.generic.AutoDerivation
import io.scalaland.chimney.Transformer

import java.time.{Instant, LocalDateTime, ZoneOffset}

object model {

  case class RawLesson(
    debug: String,
    name: String,
    group: String,
    teacher: String,
    date: List[String],
    faculty: String,
    denna: Boolean,
    `type`: String,
    auditorium: String,
    course: List[String]
  )
  object RawLesson extends AutoDerivation

  case class RawResponse(
    timestamp: Long,
    parsingErrors: Int,
    lessons: List[RawLesson]
  )
  object RawResponse extends AutoDerivation

  def round1(x: Double): Double = (x * 10).round.toDouble / 10

  implicit val tr0: Transformer[String, LocalDateTime] = (src: String) =>
    LocalDateTime.ofInstant(
      Instant.ofEpochSecond(src.toInt),
      ZoneOffset.ofHours(3)
    )

//  implicit val tr1: Transformer[RawItem, Item] = Transformer.define[RawItem, Item]
//    .withFieldComputed(_.value, r => round1(r.value.toDouble))
//    .buildTransformer
//
//  implicit val tr2: Transformer[RawData, Data] = Transformer.derive[RawData, Data]
//
//  implicit val tr3: Transformer[Data, DataWattOnly] = Transformer.derive[Data, DataWattOnly]
//
//  implicit val tr4: Transformer[DataLine, DataLineWattOnlyDetailed] = Transformer.derive[DataLine, DataLineWattOnlyDetailed]
//
//  implicit val tr6 = Transformer.define[DataLineWattOnlyDetailed, DataLineWattOnlyShort]
//    .withFieldComputed(_.W1, _.data.W1.value)
//    .withFieldComputed(_.W2, _.data.W2.value)
//    .withFieldComputed(_.W3, _.data.W3.value)
//    .withFieldComputed(_.W, _.data.W.value)
//    .buildTransformer

}
