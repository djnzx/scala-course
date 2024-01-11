package graphs.lee

case class Point (x: Int, y: Int) {

  override def toString: String = "[%d,%d]".formatted(x, y)

  def move(dx: Int, dy: Int): Point = Point(x + dx, y + dy)

}
