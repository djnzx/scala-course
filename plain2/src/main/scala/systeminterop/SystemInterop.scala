package systeminterop

import scala.sys.process._

object SystemInterop extends App {

  val process = Process("g", Seq("-la"))
  val outcome = process.!!.trim

  println("--")
  println(Console.RED + outcome + Console.RESET)
  println("--")

}
