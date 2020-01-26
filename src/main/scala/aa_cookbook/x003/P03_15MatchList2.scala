package aa_cookbook.x003

object P03_15MatchList2 extends App {
  val l1 = List(1,2,3)

  val sum: List[Int] => Int = {
    case first :: rest => first + sum(rest)
    case Nil => 0
  }

  val size: List[Int] => Int = {
    case _ :: rest => 1 + size(rest)
    case Nil => 0
  }

  println(sum(l1))
  println(size(l1))

}
