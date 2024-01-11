package graphs.lee

class LeeImmutable(val width: Int, val height: Int, obstacles: Set[Pt]) {

  def isOnBoard(p: Pt): Boolean                                   =
    p.y >= 0 && p.y < height && p.x >= 0 && p.x < width
  def neighbours(p: Pt): Set[Pt]                                  =
    Set((-1, 0), (0, -1), (1, 0), (0, 1)).map(p.move).filter(isOnBoard)
  def neighboursUnvisited(p: Pt, visited: List[Set[Pt]]): Set[Pt] =
    neighbours(p).filterNot(p => visited.exists(_.contains(p))) -- obstacles

  def backtrace(state: List[Set[Pt]], path: List[Pt] = Nil): Iterable[Pt] = (state, path) match {
    case (Nil, _)            => path                                              // finish
    case (h :: t, Nil)       => backtrace(t, h.head :: Nil)                       // first step
    case (h :: t, prev :: _) => backtrace(t, (neighbours(prev) & h).head :: path) // nest step
  }

  def trace(src: Pt, dst: Pt): (Option[Iterable[Pt]], List[Set[Pt]]) = {

    def doTrace(state: List[Set[Pt]]): (Option[Iterable[Pt]], List[Set[Pt]]) = state match {
      case Nil                       => doTrace(Set(src) :: Nil)                                   // first step
      case h :: _ if h.isEmpty       => (None, state)                                              // finish - not found
      case h :: _ if h.contains(dst) => (Some(backtrace(state)), state)                            // finish - found)
      case h :: _                    => doTrace(h.flatMap(neighboursUnvisited(_, state)) :: state) // next step
    }

    doTrace(Nil)
  }

  def represent(path0: Option[Iterable[Pt]], state: List[Set[Pt]]): String = {
    import graphs.lee.Pretty.colorize

    val path: Set[Pt] = path0 match {
      case Some(value) => value.toSet
      case None        => Set.empty
    }

    val steps = state.reverse.zipWithIndex.flatMap { case (pts, idx) => pts.map(p => (p, idx)) }.toMap

    def fmtCell(p: Pt): String = {
      lazy val vF = "%3d".format(steps.getOrElse(p, 0))
      if (obstacles.contains(p)) colorize(" XX", Console.BLUE)
      else if (path.contains(p)) colorize(vF, Console.RED)
      else vF
    }

    (0 until height)
      .map { y =>
        (0 until width)
          .map(x => Pt(x, y))
          .map(fmtCell)
          .mkString
      }
      .mkString("\n")

  }

}
