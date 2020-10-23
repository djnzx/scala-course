package whg

sealed trait Color extends Product
case object White extends Color
case object Black extends Color

object Color {
  val another: Color => Color = {
    case White => Black
    case Black => White
  }
}
