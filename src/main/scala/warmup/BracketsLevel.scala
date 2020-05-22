package warmup

object BracketsLevel extends App {
  /**
    * we are going to store intermediate
    * calculation results in the tuple (Int, Int)
    * t._1 - current nesting level
    * t._2 - max nesting level
    */
  def level(origin: String): Int =
  //          (current level, max level)
    origin.toSeq.foldLeft((0, 0))((t, el) => {
      val cur = t._1 + (el match {
        case '(' => +1
        case ')' => -1
      })
      val max = scala.math.max(t._2, cur)
      (cur, max)
    }
    )._2

  Map(
    "()()()" -> 1,
    "()" -> 1,
    "()(()())()" -> 2,
    "()(()())((()()))(())()" -> 3,
  ).foreach(t => assert(t._2 == level(t._1)))
}
