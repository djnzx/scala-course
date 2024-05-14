package app.api

import enumeratum.EnumEntry.Camelcase
import enumeratum.{Enum, EnumEntry}
import json.ZioJsonEnum

sealed trait PropVal extends EnumEntry with Camelcase

object PropVal extends Enum[PropVal] with ZioJsonEnum[PropVal] {

  case object SportId                    extends PropVal
  case object CompetitionId              extends PropVal
  case object RegionId                   extends PropVal
  case object MatchId                    extends PropVal
  case object MarketTypeId               extends PropVal
  case object SelectionTypeId            extends PropVal
  case object TypeId                     extends PropVal
  case object TotalCoefficient           extends PropVal
  case object CoefficientOfAllSelections extends PropVal
  case object NumberOfSelections         extends PropVal

  override def values: IndexedSeq[PropVal] = findValues

}
