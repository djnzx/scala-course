package interruptible

import cats.effect._
import cats.effect.std._
import cats.implicits._
import scala.concurrent.duration.DurationInt

/** - https://github.com/typelevel/cats-effect/issues/1793
  * - https://github.com/typelevel/cats-effect/issues/3077
  * - https://github.com/typelevel/cats-effect/pull/3465
  */
object ConsoleTimeoutCE3 extends IOApp.Simple {

  /** doesn't work due to the nature of JVM */
//  val readLine: IO[String] = IO(StdIn.readLine())
  /** works because it's actually async */
  val readLine: IO[String] = Console[IO].readLine

  def printLine(line: String): IO[Unit] = IO(println(line))

  val readLineTimed: IO[String] = readLine.timeoutTo(5.seconds, printLine("was loo long") >> "default".pure[IO])

  val app = printLine("enter something in 5 seconds:") >> readLineTimed >>= printLine

  override def run: IO[Unit] = app
}
