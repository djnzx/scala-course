package glovo.q

class TaskBScalaITailRec {
  val VISITED = Int.MaxValue
  case class Pt(x: Int, y: Int) 
  val DELTAS = Set(Pt(0, 1), Pt(0,-1), Pt( 1,0), Pt(-1,0))
  
  var board: Array[Array[Int]] = _
  lazy val HEIGHT = board.length
  lazy val WIDTH = board(0).length
  def move(pt: Pt, dt: Pt) = Pt(pt.x + dt.x, pt.y + dt.y)
  def markVisited(pts: Set[Pt]): Unit = pts.foreach(p => board(p.y)(p.x) = VISITED)
  def isUnvisited(p: Pt) = board(p.y)(p.x) != VISITED
  def inRange(x: Int, l: Int, h: Int) = x >= l && x < h
  def isOnBoard(p: Pt) = inRange(p.x, 0, WIDTH) && inRange(p.y, 0, HEIGHT)
  def isCountry(p: Pt, country: Int) = board(p.y)(p.x) == country
  def neighbours(p: Pt) = DELTAS.map(move(p, _)).filter(isOnBoard)
  def neighboursUnvisitedMy(pts: Set[Pt], country: Int) = 
    pts.flatMap(neighbours).filter(isUnvisited).filter(p => isCountry(p, country))
  def flood(p: Pt) = {
    val country = board(p.y)(p.x)
    def floodOne(step: Set[Pt]): Unit =
      if (step.nonEmpty) {
        markVisited(step)
        floodOne(neighboursUnvisitedMy(step, country))
      }
    floodOne(Set(p))
  }
  /**
    * this implementation 
    * DOES MUTATE the original array !
    */
  def solution(a: Array[Array[Int]]): Int =
    { board = a; a }.indices.flatMap(y => a(0).indices.map(x => Pt(x, y))) withFilter isUnvisited map flood length
}
