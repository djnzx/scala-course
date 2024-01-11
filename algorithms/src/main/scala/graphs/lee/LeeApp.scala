package graphs.lee

object LeeApp extends App {

  val src       = Pt(0, 0)
  val dst       = Pt(14, 0)
  val obstacles = Set(
    (3, 0),
    (3, 1),
    (3, 2),
    (3, 3),
    (3, 4),
    (3, 5),
    (7, 3),
    (7, 4),
    (7, 5),
    (7, 6),
    (7, 7),
    (7, 8),
    (7, 9),
    (12, 0),
    (12, 1),
    (12, 2),
    (12, 3),
    (12, 4),
  ).map { case (x, y) => Pt(x, y) }
  val lee       = new LeeMutable(15, 10, obstacles)
  val trace     = lee.trace(src, dst)
  System.out.println(trace)
  System.out.println()
  trace.foreach(path => System.out.println(lee.fmtBoard(path)))

}
