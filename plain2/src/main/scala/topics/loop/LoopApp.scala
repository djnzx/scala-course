package topics.loop

import scala.annotation.tailrec

object LoopApp extends App {

  main_loop

  @tailrec
  def main_loop: Unit = {
    //...
    scala.io.StdIn.readLine match {
      case "q" => scala.Console.println("Quitting...")
      case s: String => {
        scala.Console.println(s)
        main_loop
      }
    }
  }
}
