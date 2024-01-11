package graphs.lee

object LeeApp extends App {

  val src       = Point(0, 0)
  val dst       = Point(19, 0)
  val obstacles = Set(
    (5, 14),
    (5, 13),
    (5, 12),
    (5, 11),
    (5, 10),
    (5, 9),
    (5, 8),
    (10, 0),
    (10, 1),
    (10, 2),
    (10, 3),
    (10, 4),
    (10, 5),
    (10, 6),
    (10, 7),
    (10, 8)
  ).map { case (x, y) => Point(x, y) }
  val lee       = new Lee(20, 15, obstacles)
  val trace     = lee.trace(src, dst)
  System.out.println(trace)
  System.out.println()
  trace.foreach(path => System.out.println(lee.fmtBoard(path)))

}
