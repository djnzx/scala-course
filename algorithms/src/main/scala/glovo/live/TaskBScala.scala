package glovo.live

object TaskBScala {

  def isValid(s: String): Boolean = {
    if (s == null || s.length %2 != 0) return false
    val LEN = s.length
    val open = Set('(','[','{')
    val pairs = Map(
      '}' -> '{',
      ']' -> '[',
      ')' -> '(',
    )

    def process(i: Int, stack: List[Int]): Boolean = i match {
      case LEN => true
      case _ if open.contains(s(i)) => process(i + 1, s(i) :: stack)
      case _ if stack.isEmpty => false
      case _ if pairs.get(s(i)).contains(stack.head) => process(i + 1, stack.tail)
      case _ => false
    }

    process(0, Nil)
  }
  
}
