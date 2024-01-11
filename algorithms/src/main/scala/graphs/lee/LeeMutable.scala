package graphs.lee

/** this is implementation described
  * https://en.wikipedia.org/wiki/Lee_algorithm
  * https://ru.wikipedia.org/wiki/Алгоритм_Ли
  * http://www.eecs.northwestern.edu/~haizhou/357/lec6.pdf
  */
class LeeMutable(width: Int, height: Int, os: Iterable[Pt]) {
  import LeeMutable._
  private val board: Array[Array[Int]] = Array.fill(height, width)(EMPTY)
  putObstacles(os)

  def get(x: Int, y: Int): Int                      = board(y)(x)
  def set(x: Int, y: Int, value: Int): Unit         = board(y)(x) = value
  def get(pt: Pt): Int                              = get(pt.x, pt.y)
  def set(pt: Pt, value: Int): Unit                 = set(pt.x, pt.y, value)
  def isOnBoard(p: Pt)                              = p.y >= 0 && p.y < height && p.x >= 0 && p.x < width
  def isUnvisited(p: Pt)                            = get(p) == EMPTY
  val deltas                                        = Set((-1, 0), (0, -1), (1, 0), (0, 1))
  def neighbours(p: Pt)                             = deltas.map { case (dx, dy) => p.move(dx, dy) }.filter(isOnBoard)
  def neighboursUnvisited(p: Pt)                    = neighbours(p).filter(isUnvisited)
  def neighboursByValue(p: Pt, v: Int)              = neighbours(p).filter(p => get(p) == v)
  def putObstacles(os: Iterable[Pt])                = os.foreach(p => set(p, OBSTACLE))
  def trace(src: Pt, dst: Pt): Option[Iterable[Pt]] = {

    def flood(cur: Set[Pt], curVal: Int): Option[Unit] =
      if (cur.isEmpty) None
      else {
        cur.foreach(p => set(p, curVal))
        if (cur.contains(dst)) Some(()) // finalized
        else { // next step
          val next = cur.flatMap(neighboursUnvisited)
          flood(next, curVal + 1)
        }
      }

    def reconstruct(cur: Pt, path: List[Pt]): Iterable[Pt] = get(cur) match {
      case START => path
      case v     =>
        val prev = neighboursByValue(cur, v - 1).head
        reconstruct(prev, prev :: path)
    }

    flood(Set(src), 1).map(_ => reconstruct(dst, List(dst)))
  }

  def fmtBoard(path0: Iterable[Pt] = Iterable.empty) = {
    import graphs.lee.Pretty.colorize
    val path           = path0.toSet
    def fmtCell(p: Pt) = get(p) match {
      case OBSTACLE => colorize(" XX", Console.BLUE)
      case v        =>
        val valueF = "%3d".format(v)
        if (path.contains(p)) colorize(valueF, Console.RED)
        else valueF
    }
    board.indices
      .map(y => board(y).indices.map(x => Pt(x, y)).map(fmtCell).mkString)
      .mkString("\n")
  }
  override def toString: String                      = fmtBoard()
}

private object LeeMutable {
  val EMPTY    = 0
  val START    = 1
  val OBSTACLE = -10
}
