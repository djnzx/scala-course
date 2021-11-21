package shapelss.deserial.seq

sealed trait DecodeError
case object DeSequenceIsEmpty extends DecodeError
case object DeSequenceNonEmpty extends DecodeError
case object DeChar extends DecodeError
case object DeInt extends DecodeError
case object DeLong extends DecodeError
case object DeFloat extends DecodeError
case object DeDouble extends DecodeError
case object DeBoolean extends DecodeError
