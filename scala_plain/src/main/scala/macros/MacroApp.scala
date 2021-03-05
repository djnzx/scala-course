package macros

object MacroApp {

  import MacrosIntro._


  def main(args: Array[String]): Unit = {
//    hello()
//    print_param(33)

    val x = "G"
    val y = "K"
//    debug(x + y)
//    debug(x + y)
    debug(for {
      x <- List(1,2)
      y <- List("a","b")
    } yield (x,y))
//    val ss: Range.Inclusive = 1 to 3
//    val ss1: List[Int] = ss.toList
//    debug(
//      for(i <- ss1) {
//         println(i)
//      }
//    )
  }
  
  
  
}
