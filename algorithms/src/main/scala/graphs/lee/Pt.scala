package graphs.lee

case class Pt(x: Int, y: Int) {
  def move(dx: Int, dy: Int): Pt = Pt(x + dx, y + dy)
  def move(d: (Int, Int)): Pt    = d match { case (dx, dy) => move(dx, dy) }
  override def toString: String  = "[%d,%d]".format(x, y)
}

object Pt {
  // just for java compatibility
  def of(x: Int, y: Int): Pt = apply(x, y)
  def of(xy: (Int, Int)): Pt = xy match { case (dx, dy) => of(dx, dy) }
}
