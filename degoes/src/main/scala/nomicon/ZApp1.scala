package nomicon

import zio._
import zio.clock._
import zio.duration._

import scala.io.StdIn

object ZApp1 extends App { // this is an App from ZIO

  // Task: ANY(NO) resource, fails with ANY Throwable, returns A
  val readLine: Task[String] =
    ZIO.effect(StdIn.readLine())

  val r2: Task[(String, String)] = readLine.zip(readLine)
  val ri: Task[Int] = readLine.map(_.toInt)
  val rd: Task[Double] = readLine.map(_.toDouble)
  val zl: Task[Int] = ri.zipLeft(rd)
  val zr: Task[Double] = ri.zipRight(rd)
  
  // actually map ???
  val printNumbers: Task[List[Unit]] = ZIO.foreach((1 to 10).toList) { n =>
    printLine(n.toString)
  }

  // collect, or just creation
  val collected: Task[Seq[Unit]] = ZIO.collectAll(Seq(
    printLine("a"),
    printLine("b"),
    printLine("c"),
  ))
  
  // composes 2 results by given function. run sequentially
  val read2lines: Task[(String, String)] = readLine.zipWith(readLine)(_->_)
  
  // Task returns Nothing, 
  def printLine(line: String): Task[Unit] =
    ZIO.effect(println(line))
  
  // combination
  val echo: ZIO[Any, Throwable, Unit] = // Task[Unit]
    readLine.flatMap(line => printLine(line))
  
  // plain Scala syntax
  val echo2: Task[Unit] =
    for {
      line <- readLine
      _    <- printLine(line)
    } yield ()
  /** effect */
  def goShopping =
    ZIO.effect(println("Going to the grocery store"))

  val goShoppingLater: ZIO[Any with Clock, Throwable, Unit] = goShopping.delay(5.seconds)
  
  // U - means cant fail, Resource - ZENV
  // type ZEnv = Clock + Console + System + Random + Blocking
  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    goShoppingLater.exitCode
    
  for {
    a <- Seq(1,2,3) // flatmap
    b <- Seq(1,2,3) // map
    if (a!=b)       // withFilter (lazy)
  } yield (a,b)
}
