package graphs.lee

class LeeImmutable(width: Int, height: Int, obstacles: Set[Pt]) {

  private def isOnBoard(p: Pt)  = p.y >= 0 && p.y < height && p.x >= 0 && p.x < width
  private def neighbours(p: Pt) = Set((-1, 0), (0, -1), (1, 0), (0, 1)).map(p.move).filter(isOnBoard)
  private def neighboursUnvisited(
      p: Pt,
      visited: List[Set[Pt]]
    ) = neighbours(p).filterNot(p => visited.exists(_.contains(p))) -- obstacles

  private def backtrace(state: List[Set[Pt]], path: List[Pt] = Nil): Iterable[Pt] = state match {
    case Nil          => path
    case next :: tail => backtrace(tail, next.head :: path)
  }

  def trace(src: Pt, dst: Pt): Option[Iterable[Pt]] = {

    def doTrace(state: List[Set[Pt]]): Option[Iterable[Pt]] = state match {
      case Nil                       => doTrace(Set(src) :: Nil)                                   // first step
      case h :: _ if h.isEmpty       => None                                                       // finish - not found
      case h :: _ if h.contains(dst) => Some(backtrace(state))                                     // finish - found
      case h :: _                    => doTrace(h.flatMap(neighboursUnvisited(_, state)) :: state) // next step
    }

    doTrace(Nil)
  }

}
