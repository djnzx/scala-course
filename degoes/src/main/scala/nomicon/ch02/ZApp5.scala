package nomicon.ch02

import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console
import zio.duration._

object ZApp5 extends App {

  /** generic code, hiding fact, that we access console, and do side-effects */
  def printNumber0(n: Int) = ZIO.effect(println(n))
  
  /** idiomatic way, expressing the fact, that we need the Console */
  def printNumber(n: Int): URIO[Console, Unit] = console.putStrLn(n.toString)
  /** idiomatic way, exposing the fact, that we need the Clock */
  def sleep(t: Duration): URIO[Clock, Unit] = clock.sleep(t)
  /** we combined two effects, and result type is that we need Clock and Console as a Resource */
  def sleepAndPrint(t: Duration)(n: Int): URIO[Console with Clock, Unit] = sleep(t) *> printNumber(n)
  /** currying, and leaving only one parameter to pass */
  val sleep3andPrint: Int => URIO[Console with Clock, Unit] = sleepAndPrint(3.seconds)
  /** folding by scala, combining by ZIO */
  def appClock = (1 to 5).map(sleep3andPrint)
    .foldLeft(sleep3andPrint(0))((a, i) => a *> i)
  /** getStrLn can cause IOException */
  def readLine = console.getStrLn
  /** convert String to Int */
  def toInt(s: String) = try {
    Right(s.toInt)
  } catch {
    case x: NumberFormatException => Left(x) 
  }
  /** proper way to convert String to Int */
  def toIntZIO(s: String): IO[NumberFormatException, Int] = ZIO.fromEither(toInt(s)) 
  /** now Scala has found the most common type for NumberFormatException and IOException */
  def readInt = readLine.flatMap(toIntZIO)
  
  val appEnv = system.envs
    .map(_.map { case (k, v) => s"$k => $v"}.mkString("\n") )
    .flatMap(s => console.putStrLn(s))
  
  val tName = () => Thread.currentThread().getName
  /** we expose that we need only Console to run this */
  def printCurrThread(n: String): URIO[Console, Unit] = console.putStrLn(s"$n: ${tName()}") 
  /** we expose that we also need a Blocking to run this*/
  def printCurrThreadBlocking(n: String): ZIO[Console with Blocking, Nothing, Unit] = blocking.blocking(printCurrThread(n)) 
  
  val app = for {
    _ <- printCurrThread("default")          // run in the default non-blocking async thread pool
    _ <- printCurrThreadBlocking("blocking") // run in ThreadPool for blocking ops
  } yield ()
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
