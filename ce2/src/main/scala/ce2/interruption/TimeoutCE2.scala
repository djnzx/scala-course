package ce2.interruption

import cats.effect._
import cats.implicits._
import scala.concurrent.duration.DurationInt
import scala.io.StdIn

object TimeoutCE2 extends IOApp.Simple {
  val readLine: IO[String] = IO(StdIn.readLine())
  def printLine(line: String): IO[Unit] = IO(println(line))
  val readLineTimed: IO[String] = readLine.timeoutTo(5.seconds, printLine("was loo long") >> "default".pure[IO])
  val app = printLine("enter something in 5 seconds:") >> readLineTimed >>= printLine

  override def run: IO[Unit] = app
}
