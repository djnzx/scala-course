package _ait

/**
 * 3339 Task1Scala.class
 * 8185 Task1Scala$.class
 * 5024 Task1Scala$Pt.class
 * 1920 Task1Scala$Pt$.class
 */
object Task1Scala {

  type Surface = Array[Array[Int]]

  def isLand(p: Pt, sf: Surface) = sf(p.y)(p.x) == 1

  case class Pt(x: Int, y: Int) {
    private lazy val l  = Pt(x - 1, y)
    private lazy val r  = Pt(x + 1, y)
    private lazy val u  = Pt(x, y - 1)
    private lazy val d  = Pt(x, y + 1)
    lazy val neighbours = List(l, r, u, d)
  }

  def insideBoard(p: Pt, sf: Surface) =
    sf.indices.contains(p.y) && sf(0).indices.contains(p.y)

  def doCount(pt: Pt, sf: Surface, island: Set[Pt]): Set[Pt] =
    if (island.contains(pt)) Set.empty
    else
      (island + pt) ++ pt.neighbours
        .filter(insideBoard(_, sf))
        .filter(isLand(_, sf))
        .flatMap(doCount(_, sf, island + pt))

  def maxIslandArea(sf: Surface): Int =
    sf.indices
      .flatMap { y =>
        sf(0).indices
          .map(Pt(_, y))
          .filter(isLand(_, sf))
          .map(doCount(_, sf, Set.empty).size)
      }
      .maxOption
      .getOrElse(0)

  val surface = Array(
    Array(0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
    Array(0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0),
    Array(0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0)
  )

  def main(xs: Array[String]) =
    println(maxIslandArea(surface))

}
