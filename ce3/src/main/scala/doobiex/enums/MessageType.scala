package doobiex.enums

import enumeratum._
import enumeratum.EnumEntry._

sealed trait MessageType extends EnumEntry with Lowercase

object MessageType extends Enum[MessageType] with CirceEnum[MessageType] with DoobieEnum[MessageType] {

  case object Trace extends MessageType
  case object Debug extends MessageType
  case object Info extends MessageType
  case object Warn extends MessageType
  case object Error extends MessageType
  case object Fatal extends MessageType

  override def values: IndexedSeq[MessageType] = findValues
}
