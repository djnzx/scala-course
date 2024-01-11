package graphs.lee

// TODO: implement without mutable board
class Lee(width: Int, height: Int, os: Iterable[Point]) {
  import Lee._
  private val board: Array[Array[Int]] = Array.fill(height, width)(EMPTY)
  putObstacles(os)

  def get(x: Int, y: Int): Int                               = board(y)(x)
  def set(x: Int, y: Int, value: Int): Unit                  = board(y)(x) = value
  def get(pt: Point): Int                                    = get(pt.x, pt.y)
  def set(pt: Point, value: Int): Unit                       = set(pt.x, pt.y, value)
  def isOnBoard(p: Point)                                    = p.y >= 0 && p.y < height && p.x >= 0 && p.x < width
  def isUnvisited(p: Point)                                  = get(p) == EMPTY
  val deltas                                                 = Set((-1, 0), (0, -1), (1, 0), (0, 1))
  def neighbours(p: Point)                                   = deltas.map { case (dx, dy) => p.move(dx, dy) }.filter(isOnBoard)
  def neighboursUnvisited(p: Point)                          = neighbours(p).filter(isUnvisited)
  def neighboursByValue(p: Point, v: Int)                    = neighbours(p).filter(p => get(p) == v)
  def putObstacles(os: Iterable[Point])                      = os.foreach(p => set(p, OBSTACLE))
  def trace(src: Point, dst: Point): Option[Iterable[Point]] = {

    def flood(cur: Set[Point], curVal: Int): Option[Unit] =
      if (cur.isEmpty) None
      else {
        cur.foreach(p => set(p, curVal))
        if (cur.contains(dst)) Some(()) // finalized
        else { // next step
          val next = cur.flatMap(neighboursUnvisited)
          flood(next, curVal + 1)
        }
      }

    def reconstruct(cur: Point, path: List[Point]): Iterable[Point] = get(cur) match {
      case START => path
      case v     =>
        val prev = neighboursByValue(cur, v - 1).head
        reconstruct(prev, prev :: path)
    }

    flood(Set(src), 1).map(_ => reconstruct(dst, List(dst)))
  }

  def fmtBoard(path0: Iterable[Point] = Iterable.empty) = {
    val path              = path0.toSet
    def fmtCell(p: Point) = get(p) match {
      case OBSTACLE => colorize(" XX", Console.BLUE)
      case v        =>
        val valueF = "%3d".formatted(v)
        if (path.contains(p)) colorize(valueF, Console.RED)
        else valueF
    }
    board.indices
      .map(y => board(y).indices.map(x => Point(x, y)).map(fmtCell).mkString)
      .mkString("\n")
  }
  override def toString: String                         = fmtBoard()
}

private object Lee {
  val EMPTY    = 0
  val START    = 1
  val OBSTACLE = -10

  def colorize(value: String, color: String) = color.concat(value).concat(Console.RESET)
}
