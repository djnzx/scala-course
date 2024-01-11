package graphs.lee

case class Pt(x: Int, y: Int) {

  override def toString: String = "[%d,%d]".format(x, y)

  def move(dx: Int, dy: Int): Pt = Pt(x + dx, y + dy)

}
