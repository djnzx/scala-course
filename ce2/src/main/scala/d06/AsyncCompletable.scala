package d06

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import common.debug.DebugHelper

import java.util.concurrent.CompletableFuture
import scala.jdk.FunctionConverters.enrichAsJavaBiFunction

object AsyncCompletable extends IOApp {

  val cf: CompletableFuture[String] = CompletableFuture.supplyAsync { () => "woo!" }

  def cfToIo[A](cfa: IO[CompletableFuture[A]]): IO[A] =
    cfa.flatMap { fa =>
      IO.async { cb =>
        val handler: (A, Throwable) => Unit = {
          case (a, null) => cb(Right(a))
          case (null, t) => cb(Left(t))
        }

        fa.handle(handler.asJavaBiFunction)

        ()
      }
    }

  val app: IO[String] = cfToIo(IO(cf))

  override def run(args: List[String]): IO[ExitCode] = app
    .debug
    .as(ExitCode.Success)
}
