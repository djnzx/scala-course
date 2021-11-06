package misc

object RecursionApp extends App {

  def sum(lst: List[Int]): Int = lst match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }

  val list = List(11,22,33,44)

  println( sum(list) )

  println(list) // List(11, 22, 33)

  val li1 = list.zipWithIndex
  println(li1) // List((11,0), (22,1), (33,2))

  val li2 = li1.map(x => (x._2, x._1))
  println(li2) // List((0,11), (1,22), (2,33))

  li2.foreach {
    case (index, el) => println(s"index:$index, element:$el")
  }

  val x1 = 5

  val x = if (x1>5) 1 else -1
  val y = x1 match {
    case 1 => 1
    case 5 => -1
  }

  val hj = new HelloJava()
  hj.print()


}
