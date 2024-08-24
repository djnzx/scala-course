package el_meter

import io.circe.generic.AutoDerivation

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

}
