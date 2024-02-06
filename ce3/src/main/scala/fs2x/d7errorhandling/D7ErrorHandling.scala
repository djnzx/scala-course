package fss.d7errorhandling

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import java.lang.IllegalMonitorStateException
import scala.concurrent.duration.DurationInt
import scala.util.Random

object D7ErrorHandling extends IOApp.Simple {

  def calc(n: Int): IO[String] =
    n match {
      case n if n % 2 == 0 => IO.raiseError(new IllegalArgumentException(s"IEX: n = $n"))
      case n if n % 3 == 0 => IO.raiseError(new IllegalStateException(s"ISX: n = $n"))
      case n if n % 7 == 0 => IO.raiseError(new RuntimeException(s"ISX: n = $n"))
      case n => IO(s"Good: n = $n")
    }

  override def run: IO[Unit] =
    (1 to 10).toList
      .traverse { x =>
        calc(x)
          .recover {
            case x: IllegalStateException    => s"ISX recovered: ${x.getMessage}"
            case x: IllegalArgumentException => s"IAX recovered: ${x.getMessage}"
            case x                           => s"OTHER recovered: ${x.getMessage}"
          }
      }
      .flatMap { l =>
        l.foreach(x => println(x))
        IO.unit
      }

}
