package app.model

import json.OrderingSealedTrait
import zio.json.{JsonDecoder, JsonEncoder}

sealed trait PropVal

object PropVal {

  case class SportId(value: Int) extends PropVal
  object SportId {
    implicit val ord: Ordering[SportId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[SportId] =
      JsonDecoder.int.map(SportId(_))
    implicit val enc: JsonEncoder[SportId] =
      JsonEncoder.int.contramap(_.value)
  }

  case class CompetitionId(value: Int) extends PropVal
  object CompetitionId {
    implicit val ord: Ordering[CompetitionId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[CompetitionId] =
      JsonDecoder.int.map(CompetitionId(_))
    implicit val enc: JsonEncoder[CompetitionId] =
      JsonEncoder.int.contramap(_.value)
  }

  case class RegionId(value: Int) extends PropVal
  object RegionId {
    implicit val ord: Ordering[RegionId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[RegionId] =
      JsonDecoder.int.map(RegionId(_))
    implicit val enc: JsonEncoder[RegionId] =
      JsonEncoder.int.contramap(_.value)
  }

  case class MatchId(value: Int) extends PropVal
  object MatchId {
    implicit val ord: Ordering[MatchId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[MatchId] =
      JsonDecoder.int.map(MatchId(_))
    implicit val enc: JsonEncoder[MatchId] =
      JsonEncoder.int.contramap(_.value)
  }

  case class MarketTypeId(value: Int) extends PropVal
  object MarketTypeId {
    implicit val ord: Ordering[MarketTypeId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[MarketTypeId] =
      JsonDecoder.int.map(MarketTypeId(_))
    implicit val enc: JsonEncoder[MarketTypeId] =
      JsonEncoder.int.contramap(_.value)
  }

  case class SelectionTypeId(value: Int) extends PropVal
  object SelectionTypeId {
    implicit val ord: Ordering[SelectionTypeId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[SelectionTypeId] =
      JsonDecoder.int.map(SelectionTypeId(_))
    implicit val enc: JsonEncoder[SelectionTypeId] =
      JsonEncoder.int.contramap(_.value)
  }

  case class TypeId(value: Int) extends PropVal
  object TypeId {
    implicit val ord: Ordering[TypeId] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[TypeId] =
      JsonDecoder.int.map(TypeId(_))
    implicit val enc: JsonEncoder[TypeId] =
      JsonEncoder.int.contramap(_.value)
  }

  /** 0 - 99999.99999 */
  case class TotalCoefficient(value: BigDecimal) extends PropVal
  object TotalCoefficient {
    implicit val ord: Ordering[TotalCoefficient] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[TotalCoefficient] =
      JsonDecoder.scalaBigDecimal.map(TotalCoefficient(_))
    implicit val enc: JsonEncoder[TotalCoefficient] =
      JsonEncoder.scalaBigDecimal.contramap(_.value)
  }

  /** 0 - 99999.99999 */
  case class CoefficientOfAllSelections(value: BigDecimal) extends PropVal
  object CoefficientOfAllSelections {
    implicit val ord: Ordering[CoefficientOfAllSelections] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[CoefficientOfAllSelections] =
      JsonDecoder.scalaBigDecimal.map(CoefficientOfAllSelections(_))
    implicit val enc: JsonEncoder[CoefficientOfAllSelections] =
      JsonEncoder.scalaBigDecimal.contramap(_.value)
  }

  /** 0 - 99999 */
  case class NumberOfSelections(value: Int) extends PropVal
  object NumberOfSelections {
    implicit val ord: Ordering[NumberOfSelections] = Ordering.by(_.value)
    implicit val dec: JsonDecoder[NumberOfSelections] =
      JsonDecoder.int.map(NumberOfSelections(_))
    implicit val enc: JsonEncoder[NumberOfSelections] =
      JsonEncoder.int.contramap(_.value)
  }

  implicit val ordPv: Ordering[PropVal] = OrderingSealedTrait.gen

}
