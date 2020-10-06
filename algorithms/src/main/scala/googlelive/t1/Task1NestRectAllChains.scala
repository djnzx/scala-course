package googlelive.t1

/**
  * nested rectangles
  * find the longest nested chain
  * from the given set
  * (101, 1), (102, 2), (103, 3),
  * (4, 204), (5, 205),
  * (11,11), (12,12), (13,13), (14,14),
  * 
  * List((14, 14), (13, 13), (12, 12), (11, 11))
  */
object Task1NestRectAllChains {
  import Task1Domain._
  
  def allSmallerTo(rs: List[R], bigger: R) = rs.filter(gt(bigger, _))

  def inscribed(rs: List[R]): List[List[R]] = rs match {
    case Nil => List(Nil)
    case _   => rs.flatMap { bg => inscribed(allSmallerTo(rs, bg)).map(bg :: _) }
  }
  
  def longest(rs: List[List[R]]) = rs.maxBy(_.length)
}
