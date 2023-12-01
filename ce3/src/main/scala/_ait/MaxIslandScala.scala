package _ait

object MaxIslandScala {

  case class Pt(x: Int, y: Int) {
    private lazy val l  = Pt(x - 1, y)
    private lazy val r  = Pt(x + 1, y)
    private lazy val u  = Pt(x, y - 1)
    private lazy val d  = Pt(x, y + 1)
    lazy val neighbours = Set(l, r, u, d)
  }

  def maxIslandArea(sf: Array[Array[Int]]): Int = {
    val indicesY      = sf.indices
    lazy val indicesX = sf(0).indices

    def isLand(p: Pt) = sf(p.y)(p.x) == 1

    def insideBoard(p: Pt) = indicesY.contains(p.y) && indicesX.contains(p.y)

    def flood(pt: Pt, current: Set[Pt], visited: Set[Pt]): Option[Set[Pt]] =
      Option.unless(
        current.contains(pt) || // loop detection
          visited.contains(pt)  // prevent count the same island from the different point
      ) {
        val next = current + pt
        next ++ pt.neighbours
          .filter(insideBoard)
          .filter(isLand)
          .flatMap(flood(_, next, visited))
          .flatten
      }

    indicesY
      .to(LazyList)
      .flatMap { y =>
        indicesX
          .to(LazyList)
          .map(x => Pt(x, y))
          .filter(isLand)
      }
      // here we have points lazily provided
      .foldLeft((Set.empty[Pt], 0)) { case (x @ (visited, mx), p) =>
        flood(p, Set.empty, visited) match {
          case Some(found) => (visited ++ found, mx max found.size)
          case None        => x
        }
      } match {
      case (_, mx) => mx
    }
  }

  val surface = Array(
    Array(0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
    Array(0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0),
    Array(0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0)
  )

  def main(xs: Array[String]) = println(maxIslandArea(surface))

}
