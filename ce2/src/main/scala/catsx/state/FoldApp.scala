package catsx.state

import pprint.{pprintln => println}

object FoldApp extends App {

  /**
    * task:
    * 1. find: min, max, sum, count, avg
    * 2. log all odd elements to one list
    * 3. log all evens to another
    */
  val DATA = List(1,10,3,7,21,5)

  case class Proc(mn: Int, mx: Int, cnt: Int, sum: Long) {
    def consume(x: Int) = Proc(x min mn, x max mx, cnt + 1, sum + x)
    def avg = sum.toDouble / cnt
  }
  val INITIAL = Proc(Int.MaxValue, Int.MinValue, 0, 0)

  val r = DATA.foldLeft(INITIAL)(_ consume _) match {
    case p: Proc => (p, p.avg)
  }
  println(r)
}
