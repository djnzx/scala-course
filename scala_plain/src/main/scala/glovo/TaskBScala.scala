package glovo

class TaskBScala {
  val VISITED = Int.MaxValue
  case class Pt(x: Int, y: Int) 
  val DELTAS = Set(Pt(0, 1), Pt(0,-1), Pt( 1,0), Pt(-1,0))
  
  var board: Array[Array[Int]] = _
  lazy val HEIGHT = board.length
  lazy val WIDTH = board(0).length
  def move(pt: Pt, dt: Pt) = Pt(pt.x + dt.x, pt.y + dt.y)
  def markVisited(pts: Set[Pt]): Unit = pts.foreach(p => board(p.y)(p.x) = VISITED)
  def isUnvisited(p: Pt) = board(p.y)(p.x) != VISITED
  def isOnBoard(p: Pt) = p.x >= 0 && p.x < WIDTH && p.y >= 0 && p.y < HEIGHT
  def isCountry(p: Pt, country: Int) = board(p.y)(p.x) == country
  def neighbours(p: Pt) = DELTAS.map(move(p, _)).filter(isOnBoard)
  def neighboursUnvisitedMy(pts: Set[Pt], cnt: Int) = 
    pts.flatMap(neighbours).filter(isUnvisited).filter(p => isCountry(p, cnt))
  def flood(p: Pt) = {
    val country = board(p.y)(p.x)
    var step = Set(p)
    while (step.nonEmpty) {
      markVisited(step)
      step = neighboursUnvisitedMy(step, country)
    }
  }

  def solution(a: Array[Array[Int]]): Int =
    { board = a; a }.indices.flatMap(y => a(0).indices.map(x => Pt(x, y))) withFilter isUnvisited map flood length
}
