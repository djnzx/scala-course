package _ait

case class Pt(x: Int, y: Int) {
  private lazy val l  = Pt(x - 1, y)
  private lazy val r  = Pt(x + 1, y)
  private lazy val u  = Pt(x, y - 1)
  private lazy val d  = Pt(x, y + 1)
  lazy val neighbours = Set(l, r, u, d)
}

class IslandRelatedTasks(sf: Array[Array[Int]]) {

  val indicesY      = sf.indices
  lazy val indicesX = sf(0).indices

  def isLand(p: Pt): Boolean = sf(p.y)(p.x) == 1

  def insideBoard(p: Pt): Boolean = indicesY.contains(p.y) && indicesX.contains(p.x)

  def flood(pt: Pt, current: Set[Pt], visited: Set[Pt]): Option[Set[Pt]] =
    Option.unless(
      current.contains(pt) || // loop detection
        visited.contains(pt)  // double entry prevention
    ) {
      val next = current + pt
      next ++ pt.neighbours
        .filter(insideBoard)
        .filter(isLand)
        .flatMap(p => flood(p, next, visited))
        .flatten
    }

  val nonWater: Seq[Pt] = indicesY
    .to(LazyList)
    .flatMap { y =>
      indicesX
        .to(LazyList)
        .map(x => Pt(x, y))
        .filter(isLand)
    }

  def maxIslandArea: Int = nonWater
    //    max-size   visited points
    .foldLeft(0 -> Set.empty[Pt]) { case (x @ (mx, visited), p) =>
      flood(p, Set.empty, visited) match {
        case Some(found) => (mx max found.size, found ++ visited)
        case None        => x
      }
    } match {
    case (max, _) => max
  }

  def firstMaxIslandSize: Set[Pt] = nonWater
    //        max-island       visited points
    .foldLeft(Set.empty[Pt] -> Set.empty[Pt]) { case (x @ (island, visited), p) =>
      flood(p, Set.empty, visited) match {
        case Some(found) if found.size > island.size => (found, found ++ visited)
        case Some(found)                             => (island, found ++ visited)
        case _                                       => x
      }
    } match {
    case (max, _) => max
  }

  def islandCount: Int = nonWater
    //    count   visited points
    .foldLeft(0 -> Set.empty[Pt]) { case (x @ (count, visited), p) =>
      flood(p, Set.empty, visited) match {
        case Some(found) => (count + 1, found ++ visited)
        case None        => x
      }
    } match {
    case (count, _) => count
  }

}
