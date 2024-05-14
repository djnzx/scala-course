package app.api

import enumeratum.{Enum, EnumEntry}
import enumeratum.EnumEntry.Lowercase
import json.ZioJsonEnum

sealed trait Predicate extends EnumEntry with Lowercase

object Predicate extends Enum[Predicate] with ZioJsonEnum[Predicate] {

  case object eq    extends Predicate
  case object neq   extends Predicate
  case object gte   extends Predicate
  case object gt    extends Predicate
  case object lte   extends Predicate
  case object lt    extends Predicate
  case object in    extends Predicate
  case object notin extends Predicate

  override def values: IndexedSeq[Predicate] = findValues

}
