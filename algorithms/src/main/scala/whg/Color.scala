package whg

sealed trait Color extends Product {
  val another: Color = Color.another(this)
}
case object White extends Color
case object Black extends Color

object Color {
  def another: Color => Color = {
    case White => Black
    case Black => White
  }
}
