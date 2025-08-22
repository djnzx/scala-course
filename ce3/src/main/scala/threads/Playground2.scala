package threads

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.implicits.concurrentParTraverseOps
import cats.implicits._
import org.typelevel.log4cats.slf4j._
import org.typelevel.log4cats.syntax.LoggerInterpolator

object Playground2 extends IOApp.Simple {

//  // default
//  val log0 = Slf4jLogger.getLogger[IO]
//
//  // shifted to blocking
//  implicit val log = BlockingLogger.make(log0)

  implicit class ColorOps(s: String) {
    def red = fansi.Color.Red(s)
    def blue = fansi.Color.Blue(s)
    def green = fansi.Color.Green(s)
  }

  implicit class DebugOps[A](fa: IO[A]) {
    def t: IO[A] = fa.flatTap(x => IO(println(s"$x >>> on ${Thread.currentThread().getName.blue}")))
  }

  override def run: IO[Unit] =
    (1 to 20).toList
      .parTraverse_ { x =>
        IO.blocking
        { Thread.sleep(1000);x }.t
      }

}
