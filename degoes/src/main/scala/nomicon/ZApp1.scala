package nomicon

import zio._
import zio.clock._
import zio.duration._

import scala.io.StdIn

object ZApp1 extends App {

  val readLine: Task[String] =
    ZIO.effect(StdIn.readLine())
  
  def printLine(line: String) =
    ZIO.effect(println(line))
  
  val echo =
    readLine.flatMap(line => printLine(line))
  
  /** effect */
  def goShopping =
    ZIO.effect(println("Going to the grocery store"))

  val goShoppingLater = goShopping.delay(5.seconds)
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    goShoppingLater.exitCode
    
}
