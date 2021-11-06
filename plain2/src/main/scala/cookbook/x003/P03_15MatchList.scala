package cookbook.x003

object P03_15MatchList extends App {
  val l1 = List(1,2,3)
  val l2 = 1 :: 2 :: 3 :: Nil
  println(l1 == l2)
  println(l1.mkString(" "))

  def listToString1(list: List[Any]): String = list match {
    case first :: rest => first + " " + listToString1(rest)
    case Nil => ""
  }

  val listToString2: List[Any] => String = {
    case first :: rest => first + " " + listToString2(rest)
    case Nil => ""
  }

  val listToString3: List[Any] => String = list => list match {
    case first :: rest => first + " " + listToString3(rest)
    case Nil => ""
  }

  println(listToString1(l1))
  println(listToString2(l1))
  println(listToString3(l1))

}
