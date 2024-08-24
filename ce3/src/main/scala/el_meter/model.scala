package el_meter

import io.circe.generic.AutoDerivation
import io.scalaland.chimney.Transformer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object model {

  case class Separator(
    name: String
  )
  object Separator extends AutoDerivation

  case class RawItem(
    name: String,
    unit: String,
    value: String
  )
  object RawItem extends AutoDerivation

  case class RawData(
    V1: RawItem,
    A1: RawItem,
    W1: RawItem,
    rW1: RawItem,
    Wh1: RawItem,
    PF1: RawItem,
    Fr1: RawItem,
    br0: Separator,
    V2: RawItem,
    A2: RawItem,
    W2: RawItem,
    rW2: RawItem,
    Wh2: RawItem,
    PF2: RawItem,
    Fr2: RawItem,
    br1: Separator,
    V3: RawItem,
    A3: RawItem,
    W3: RawItem,
    rW3: RawItem,
    Wh3: RawItem,
    PF3: RawItem,
    Fr3: RawItem,
    br2: Separator,
    A: RawItem,
    W: RawItem,
    rW: RawItem,
    TWh: RawItem,
    rTWh: RawItem,
    br3: Separator,
    T: RawItem,
  )
  object RawData extends AutoDerivation

  case class RawResponse(
    devid: String,
    time: String,
    pout: String,
    powset: String,
    data: RawData
  )
  object RawResponse extends AutoDerivation

  case class Item(
    name: String,
    unit: String,
    value: Double
  )

  case class Data(
    V1: Item,
    A1: Item,
    W1: Item,
    PF1: Item,
    //
    V2: Item,
    A2: Item,
    W2: Item,
    PF2: Item,
    //
    V3: Item,
    A3: Item,
    W3: Item,
    PF3: Item,
    //
    A: Item,
    W: Item,
  )

  case class DataLine(
    time: LocalDateTime,
    data: Data
  )

  case class DataWattOnly(
    W1: Item,
    W2: Item,
    W3: Item,
    W: Item,
  )
  case class DataLineWattOnlyDetailed(
    time: LocalDateTime,
    data: DataWattOnly
  )

  case class DataLineWattOnlyShort(
    time: LocalDateTime,
    W1: Double,
    W2: Double,
    W3: Double,
    W: Double,
  )

  def round1(x: Double): Double = (x * 10).round.toDouble / 10

  implicit val tr0: Transformer[String, LocalDateTime] = (src: String) =>
    LocalDateTime.ofInstant(
      Instant.ofEpochSecond(src.toInt),
      ZoneOffset.ofHours(3)
    )

  implicit val tr1: Transformer[RawItem, Item] = Transformer.define[RawItem, Item]
    .withFieldComputed(_.value, r => round1(r.value.toDouble))
    .buildTransformer

  implicit val tr2: Transformer[RawData, Data] = Transformer.derive[RawData, Data]

  implicit val tr3: Transformer[Data, DataWattOnly] = Transformer.derive[Data, DataWattOnly]

  implicit val tr4: Transformer[DataLine, DataLineWattOnlyDetailed] = Transformer.derive[DataLine, DataLineWattOnlyDetailed]

  implicit val tr6 = Transformer.define[DataLineWattOnlyDetailed, DataLineWattOnlyShort]
    .withFieldComputed(_.W1, _.data.W1.value)
    .withFieldComputed(_.W2, _.data.W2.value)
    .withFieldComputed(_.W3, _.data.W3.value)
    .withFieldComputed(_.W, _.data.W.value)
    .buildTransformer

}
