package app.model

import enumeratum.EnumEntry.Lowercase
import enumeratum.{Enum, EnumEntry}
import json.ZioJsonEnum

sealed trait Relation extends EnumEntry with Lowercase

object Relation extends Enum[Relation] with ZioJsonEnum[Relation] {
  case object All extends Relation
  case object Any extends Relation

  override def values: IndexedSeq[Relation] = findValues
}
