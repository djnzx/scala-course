package macros

object MacroApp {

  import MacrosIntro._


  def main(args: Array[String]): Unit = {
    hello()
    print_param(33)
    
    val x = "G"
    val y = "K"
    debug(x + y)
    debug(for {
      x <- List(1,2)
      y <- List("a","b")
    } yield (x,y))
  }
  
  
  
}
