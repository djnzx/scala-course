package app.model

import zio.json.JsonDecoder

/** since Predicate is agnostic we need to have the way to pattern match during validation
  */
sealed trait Condition[+A]

object Condition {

  /** for matching in validation */
  case class SportId(operator: Predicate[PropVal.SportId])
      extends Condition[PropVal.SportId]

  case class CompetitionId(operator: Predicate[PropVal.CompetitionId])
      extends Condition[PropVal.CompetitionId]

  case class RegionId(operator: Predicate[PropVal.RegionId])
      extends Condition[PropVal.RegionId]

  case class MatchId(operator: Predicate[PropVal.MatchId])
      extends Condition[PropVal.MatchId]

  case class MarketTypeId(operator: Predicate[PropVal.MarketTypeId])
      extends Condition[PropVal.MarketTypeId]

  case class SelectionTypeId(operator: Predicate[PropVal.SelectionTypeId])
      extends Condition[PropVal.SelectionTypeId]

  case class TypeId(operator: Predicate[PropVal.TypeId])
      extends Condition[PropVal.TypeId]

  case class TotalCoefficient(operator: Predicate[PropVal.TotalCoefficient])
      extends Condition[PropVal.TotalCoefficient]

  case class CoefficientOfAllSelections(
    operator: Predicate[PropVal.CoefficientOfAllSelections]
  ) extends Condition[PropVal.CoefficientOfAllSelections]

  case class NumberOfSelections(operator: Predicate[PropVal.NumberOfSelections])
      extends Condition[PropVal.NumberOfSelections]

  implicit val decoderCondPropVal: JsonDecoder[Condition[PropVal]] =
    ConditionPropVal.decoder

}
