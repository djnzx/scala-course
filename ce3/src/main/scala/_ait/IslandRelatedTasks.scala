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

  def flood(current: Set[Pt], visited: Set[Pt]): Option[Set[Pt]] =
    if (current.size == 1 && visited.contains(current.head)) None // double entry prevention
    else {
      val next = current
        .flatMap(_.neighbours)
        .filter(insideBoard)
        .filter(isLand) -- current
      if (next.isEmpty) Some(current)
      else flood(current ++ next, visited)
    }

  val nonWater: Seq[Pt] = indicesY
    .flatMap { y =>
      indicesX
        .flatMap(x => Some(Pt(x, y)).filter(isLand))
    }

  def maxIslandArea: Int = nonWater
    //    max-size   visited points
    .foldLeft(0 -> Set.empty[Pt]) { case (x @ (mx, visited), p) =>
      flood(Set(p), visited) match {
        case Some(found) => (mx max found.size, found ++ visited)
        case None        => x
      }
    } match {
    case (max, _) => max
  }

  def firstMaxIslandSize: Set[Pt] = nonWater
    //        max-island       visited points
    .foldLeft(Set.empty[Pt] -> Set.empty[Pt]) { case (x @ (island, visited), p) =>
      flood(Set(p), visited) match {
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
      flood(Set(p), visited) match {
        case Some(found) => (count + 1, found ++ visited)
        case None        => x
      }
    } match {
    case (count, _) => count
  }

}
