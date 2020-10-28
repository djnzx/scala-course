package whg

sealed trait Color extends Product {
  val another = this match {
    case White => Black
    case Black => White
  }
}
case object White extends Color
case object Black extends Color
