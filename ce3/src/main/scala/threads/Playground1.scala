package threads

import cats.effect.IO
import cats.effect.IOApp
import org.typelevel.log4cats.slf4j._
import org.typelevel.log4cats.syntax.LoggerInterpolator
import fansi.Color._

object Playground1 extends IOApp.Simple {

  // default
  val log0 = Slf4jLogger.getLogger[IO]

  // shifted to blocking
  implicit val log = BlockingLogger.make(log0)

  implicit class ColorOps(s: String) {
    def red = fansi.Color.Red(s)
    def blue = fansi.Color.Blue(s)
    def green = fansi.Color.Green(s)
  }

  implicit class DebugOps[A](fa: IO[A]) {
    def t: IO[A] = fa.flatTap(_ => IO(println(s">>> on ${Thread.currentThread().getName}".blue)))
  }

  override def run: IO[Unit] = {
    log.info(s"${Runtime.getRuntime.availableProcessors()}") >> // M4 Max = 12
    IO(println("1. should be compute")).t >>
      log0.info("2. via logger (compute)").t >>
      log.info("3. blocking logger (blocking)").t >>
      IO.blocking(println("4. explicitly on blocking")).t >>
      info"5. via implicit syntax - should pick proper blocking logging".t
  }

}
