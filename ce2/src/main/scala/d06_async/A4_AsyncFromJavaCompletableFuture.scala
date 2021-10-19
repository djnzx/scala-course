package d06_async

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import common.debug.DebugHelper

import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction
import scala.jdk.FunctionConverters.enrichAsJavaBiFunction

object A4_AsyncFromJavaCompletableFuture extends IOApp {

  /** actually we don't care how it's constructed */
  val cf: CompletableFuture[String] = CompletableFuture.supplyAsync { () => "woo!" }

  /** effect wrapped into IO to suspend */
  val wrapped: IO[CompletableFuture[String]] = IO(cf)

  def fromJavaCompletableFuture[A](cfa: IO[CompletableFuture[A]]): IO[A] =
    cfa.flatMap { cfa: CompletableFuture[A] =>
      IO.async { cb: (Either[Throwable, A] => Unit) =>
        /** by contract one of them should be null */
        val scalaHandler: (A, Throwable) => Unit = {
          case (a, null) => cb(Right(a))
          case (null, t) => cb(Left(t))
          case (a, t)    => sys.error(s"CompletableFuture handler should always have one null, got: $a, $t")
        }

        val javaHandler: BiFunction[A, Throwable, Unit] = scalaHandler.asJavaBiFunction

        cfa.handle(javaHandler)

        /** cfa.handleAsync(javaHandler)
          *
          * If registered with handleAsync,
          * then handler would execute in the CompletableFuture's thread pool,
          * and subsequent IO effects would also executed there,
          * which is probably not the expected behavior.
          * If you use handleAsync, then you need to subsequently IO.shift
          * after the IO.async call to ensure that the next effects occur in the IO context.
          */

        ()
      }
    }

  /** remapped into IO without nested stuff */
  val app: IO[String] = fromJavaCompletableFuture(wrapped)

  override def run(args: List[String]): IO[ExitCode] = app
    .debug
    .as(ExitCode.Success)
}
